package com.example.uts.ui.fragments

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uts.LauncherActivity
import com.example.uts.R
import com.example.uts.adapter.ItemTravelAdapter
import com.example.uts.database.StationDao
import com.example.uts.database.TravelDao
import com.example.uts.database.TravelDatabase
import com.example.uts.databinding.FragmentExploreTravelBinding
import com.example.uts.model.Station
import com.example.uts.model.TravelWithAllFields
import com.example.uts.model.toTravel
import com.example.uts.ui.dialogs.ConfirmationDialog
import com.example.uts.utils.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.AbstractExecutorService

class ExploreTravelFragment : Fragment() {
    private var _binding: FragmentExploreTravelBinding? = null
    private val binding get() = _binding!!
    private val travelAdapter = ItemTravelAdapter { action, travelId ->
        when (action) {
            "openEditTravelFragment" -> {
                val action = ExploreTravelFragmentDirections
                    .actionExploreTravelFragmentToEditTravelFragment(travelId)
                findNavController().navigate(action)
            }

            "openConfirmationDialog" -> {
                val deleteConfirmationDialog = ConfirmationDialog(
                    "Delete Travel",
                    "Are you sure you want to delete this travel?",
                ) {
                    travelCollection.document(travelId).delete()
                    travelDao.deleteTravelById(travelId)
                    getAllTravels()
                }
                deleteConfirmationDialog.show(parentFragmentManager, "deleteConfirmationDialog")
            }
            "orderTravel" -> {
                val orderConfirmationDialog = ConfirmationDialog(
                    "Order Travel",
                    "Are you sure you want to order this travel?",
                ) {
                    orderTravel(travelId)
                }
                orderConfirmationDialog.show(parentFragmentManager, "orderConfirmationDialog")
            }
        }
    }
    private val firestore = FirebaseFirestore.getInstance()
    private val travelCollection = firestore.collection("travels")
    private val stationCollection = firestore.collection("stations")
    private val travelOrderCollection = firestore.collection("travelOrders")
    private val channelId = "TRAVEL_APP"
    private val notificationId = 0
    private lateinit var travelDao: TravelDao
    private lateinit var stationDao: StationDao
    private lateinit var executorService: AbstractExecutorService
    private lateinit var sessionManager: SessionManager
    private lateinit var connectivityManager : ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreTravelBinding.inflate(inflater, container, false)
        sessionManager = SessionManager.getInstance(requireContext())
        val db = TravelDatabase.getDatabase(requireContext())
        travelDao = db!!.travelDao()!!
        stationDao = db.stationDao()!!
        executorService = java.util.concurrent.Executors.newSingleThreadExecutor() as AbstractExecutorService
        connectivityManager = requireContext().getSystemService(ConnectivityManager::class.java)

        setupRecyclerView()
        setCurrentUserData()
        getAllTravels()

        if (sessionManager.getUserRole() == "admin") {
            setupAddTravelButton()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        with(binding) {
            rvExploreTravel.apply {
                adapter = travelAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    private fun setCurrentUserData() {
        travelAdapter.setUserRole(sessionManager.getUserRole())
        travelAdapter.setUserId(sessionManager.getUserId())
    }

    private fun getAllTravels() {
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                super.onAvailable(network)
                travelCollection
                    .orderBy("departureDate")
                    .orderBy("departureTime")
                    .get().addOnSuccessListener { result ->
                        val travelList = result.documents.map { document ->
                            document.toTravel()
                        }
                        var originStation: Station
                        var arrivalStation: Station
                        val travelWithAllFieldsList: MutableList<TravelWithAllFields> = mutableListOf()
                        travelList.forEach { travel ->
                            executorService.execute {
                                travelDao.upsertTravel(travel)
                            }
                            stationCollection.document(travel.originStationId).get().addOnSuccessListener { result ->
                                originStation = result.toObject(Station::class.java)!!
                                stationCollection.document(travel.arrivalStationId).get().addOnSuccessListener { result ->
                                    arrivalStation = result.toObject(Station::class.java)!!
                                    travelWithAllFieldsList.add(TravelWithAllFields(travel, originStation, arrivalStation))
                                    travelAdapter.setTravelList(travelWithAllFieldsList)
                                }
                            }
                        }
                    }
            }

            override fun onLost(network: android.net.Network) {
                super.onLost(network)
                Log.d("TAG", "onLost: ")
                val travelWithAllFieldsList: MutableList<TravelWithAllFields> = mutableListOf()
                travelDao.getAllTravels().observe(viewLifecycleOwner) {
                    it.forEach { travel ->
                        val originStation = stationDao.getStationById(travel.originStationId)
                        val arrivalStation = stationDao.getStationById(travel.arrivalStationId)
                        travelWithAllFieldsList.add(TravelWithAllFields(travel, originStation, arrivalStation))
                        travelAdapter.setTravelList(travelWithAllFieldsList)
                    }
                }
            }
        })


    }

    private fun setupAddTravelButton() {
        with(binding) {
            btnAddTravel.visibility = View.VISIBLE

            btnAddTravel.setOnClickListener {
                val action = ExploreTravelFragmentDirections
                    .actionExploreTravelFragmentToCreateTravelFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun orderTravel(travelId: String) {
        val travelOrder = hashMapOf(
            "travelId" to travelId,
            "userId" to sessionManager.getUserId(),
            "date" to SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()),
            "time" to SimpleDateFormat("HH:mm:ss", Locale.US).format(Time(System.currentTimeMillis())),
            "totalPrice" to (travelAdapter.getTravelPrice(travelId)?.toInt() ?: 0)
        )

        travelOrderCollection.add(travelOrder).addOnSuccessListener {
            travelOrderCollection.document(it.id).update("travelOrderId", it.id)
        }.addOnSuccessListener {
            Toast.makeText(requireContext(), "Order success", Toast.LENGTH_SHORT).show()

            val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                0
            }

            val intent = Intent(requireContext(), LauncherActivity::class.java)
                .putExtra("MESSAGE", "Order success")
            val pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, flag)

            val notificationBuilder = NotificationCompat.Builder(requireContext(), channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Order Success")
                .setContentText("Your order has been successfully placed")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

            val nofificationManager = requireContext()
                .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(channelId, "Travel App", NotificationManager.IMPORTANCE_DEFAULT)
                with(nofificationManager) {
                    createNotificationChannel(notificationChannel)
                    notify(notificationId, notificationBuilder.build())
                }
            } else {
                nofificationManager.notify(notificationId, notificationBuilder.build())
            }


        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Order failed", Toast.LENGTH_SHORT).show()
        }
    }
}