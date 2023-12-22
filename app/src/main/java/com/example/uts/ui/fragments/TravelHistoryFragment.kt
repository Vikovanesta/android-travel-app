package com.example.uts.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uts.adapter.ItemOrderAdapter
import com.example.uts.databinding.FragmentTravelHistoryBinding
import com.example.uts.model.Station
import com.example.uts.model.Travel
import com.example.uts.model.TravelOrderWithAllFields
import com.example.uts.model.TravelWithAllFields
import com.example.uts.model.toTravel
import com.example.uts.model.toTravelOrder
import com.example.uts.utils.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class TravelHistoryFragment : Fragment() {
    private var _binding: FragmentTravelHistoryBinding? = null
    private val binding get() = _binding!!
    private val orderAdapter = ItemOrderAdapter()
    private val firestore = FirebaseFirestore.getInstance()
    private val orderCollection = firestore.collection("travelOrders")
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
        _binding = FragmentTravelHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()

        sessionManager = SessionManager.getInstance(requireContext())
        getAllOrdersById(sessionManager.getUserId())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.rvTravelHistory.apply {
            adapter = orderAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun getAllOrdersById(id: String) {
        orderCollection
            .whereEqualTo("userId", id)
            .orderBy("date", Query.Direction.DESCENDING)
            .orderBy("time", Query.Direction.DESCENDING)
            .get().addOnSuccessListener { result ->
                val orderList = result.documents.map { document ->
                    document.toTravelOrder()
                }
                var travel: Travel
                var originStation: Station
                var arrivalStation: Station
                var travelWithAllFields: TravelWithAllFields
                val travelOrderWithAllFieldsList: MutableList<TravelOrderWithAllFields> = mutableListOf()
                orderList.forEach { order ->
                    travelCollection.document(order.travelId).get().addOnSuccessListener { result ->
                        travel = result.toTravel()
                        stationCollection.document(travel.originStationId).get().addOnSuccessListener { result ->
                            originStation = result.toObject(Station::class.java)!!
                            stationCollection.document(travel.arrivalStationId).get().addOnSuccessListener { result ->
                                arrivalStation = result.toObject(Station::class.java)!!
                                travelWithAllFields = TravelWithAllFields(travel, originStation, arrivalStation)
                                travelOrderWithAllFieldsList.add(TravelOrderWithAllFields(order, travelWithAllFields))
                                orderAdapter.setOrderList(travelOrderWithAllFieldsList)
                            }
                        }
                    }
                }
            }
    }
}