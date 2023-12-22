package com.example.uts.ui.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.telephony.AvailableNetworkInfo
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Dao
import com.example.uts.R
import com.example.uts.adapter.ItemStationAdapter
import com.example.uts.database.StationDao
import com.example.uts.database.TravelDatabase
import com.example.uts.databinding.FragmentProfileBinding
import com.example.uts.databinding.FragmentStationListBinding
import com.example.uts.model.Station
import com.example.uts.utils.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.concurrent.AbstractExecutorService
import java.util.concurrent.Executors

class StationListFragment : Fragment() {
    private var _binding: FragmentStationListBinding? = null
    private val binding get() = _binding!!
    private val stationAdapter = ItemStationAdapter()
    private val firestore = FirebaseFirestore.getInstance()
    private val stationCollection = firestore.collection("stations")
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
        _binding = FragmentStationListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        sessionManager = SessionManager.getInstance(requireContext())
        val db = TravelDatabase.getDatabase(requireContext())
        stationDao = db!!.stationDao()!!
        executorService = Executors.newSingleThreadExecutor() as AbstractExecutorService
        connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        Log.d("StationListFragment", "onViewCreated: $connectivityManager")

        getAllStations()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.rvStationList.apply {
            adapter = stationAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getAllStations() {

        Log.d("StationListFragment", "getAllStations: ")

        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                super.onAvailable(network)
                stationCollection
                    .orderBy("name", Query.Direction.ASCENDING)
                    .get().addOnSuccessListener {
                        val stationList = mutableListOf<Station>()
                        for (document in it.documents) {
                            val station = document.toObject(Station::class.java)
                            station?.let { it1 -> stationList.add(it1) }
                            executorService.execute {
                                station?.let { it1 -> stationDao.upsertStation(it1) }
                            }
                        }
                        stationAdapter.setStationList(stationList)
                    }
                Log.d("StationListFragment", "onAvailable: network available")
            }

            override fun onLost(network: android.net.Network) {
                super.onLost(network)
                Log.d("StationListFragment", "onLost: network lost")
                stationDao.getAllStations().observe(viewLifecycleOwner) {
                    stationAdapter.setStationList(it)
                }
            }
        })


    }
}