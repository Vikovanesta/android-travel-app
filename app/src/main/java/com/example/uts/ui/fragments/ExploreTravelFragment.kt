package com.example.uts.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uts.adapter.ItemTravelAdapter
import com.example.uts.databinding.FragmentExploreTravelBinding
import com.example.uts.model.Station
import com.example.uts.model.TravelWithAllFields
import com.example.uts.model.toTravel
import com.example.uts.ui.dialogs.ConfirmationDialog
import com.example.uts.utils.SessionManager
import com.google.firebase.firestore.FirebaseFirestore

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
                    getAllTravels()
                }
                deleteConfirmationDialog.show(parentFragmentManager, "deleteConfirmationDialog")
            }
        }
    }
    private val firestore = FirebaseFirestore.getInstance()
    private val travelCollection = firestore.collection("travels")
    private val stationCollection = firestore.collection("stations")
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreTravelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager.getInstance(requireContext())

        setupRecyclerView()
        setCurrentUserData()
        getAllTravels()

        if (sessionManager.getUserRole() == "admin") {
            setupAddTravelButton()
        }
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
}