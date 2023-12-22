package com.example.uts.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uts.R
import com.example.uts.adapter.ItemStationAdapter
import com.example.uts.databinding.FragmentProfileBinding
import com.example.uts.databinding.FragmentStationListBinding
import com.example.uts.model.Station
import com.example.uts.utils.SessionManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class StationListFragment : Fragment() {
    private var _binding: FragmentStationListBinding? = null
    private val binding get() = _binding!!
    private val stationAdapter = ItemStationAdapter()
    private val firestore = FirebaseFirestore.getInstance()
    private val stationCollection = firestore.collection("stations")
    private lateinit var sessionManager: SessionManager

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
        stationCollection
            .orderBy("name", Query.Direction.ASCENDING)
            .get().addOnSuccessListener {
            val stationList = mutableListOf<Station>()
            for (document in it.documents) {
                val station = document.toObject(Station::class.java)
                station?.let { it1 -> stationList.add(it1) }
            }
            stationAdapter.setStationList(stationList)
        }
    }
}