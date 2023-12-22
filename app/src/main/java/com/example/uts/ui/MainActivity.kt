package com.example.uts.ui

import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.uts.R
import com.example.uts.database.TravelDao
import com.example.uts.database.TravelDatabase
import com.example.uts.databinding.ActivityMainBinding
import com.example.uts.utils.SessionManager
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private val travelCollection = firestore.collection("travels")
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var travelDao: TravelDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager.getInstance(this)
        connectivityManager = getSystemService(ConnectivityManager::class.java)
        val db = TravelDatabase.getDatabase(this)
        travelDao = db!!.travelDao()!!

        setupBottomNav()
        insertTravelToFirestore()
    }

    private fun setupBottomNav() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView = binding.bottomNavigationView

        when (sessionManager.getUserRole()) {
            "user" -> {
                Log.d("MainActivity", "setupBottomNav: user")
                navView.menu.clear()
                navView.inflateMenu(R.menu.bottom_navigation_user)
                navController.setGraph(R.navigation.bottom_navigation_user)
            }
            "admin" -> {
                Log.d("MainActivity", "setupBottomNav: admin")
                navView.menu.clear()
                navView.inflateMenu(R.menu.bottom_navigation_admin)
                navController.setGraph(R.navigation.bottom_navigation_admin)
            }
        }

        navView.setupWithNavController(navController)
    }

    private fun insertTravelToFirestore() {
        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                super.onAvailable(network)
                Log.d("MainActivity", "onAvailable: network available")
                val travels = travelDao.getAllTravels().value
                if (travels != null) {
                    for (travel in travels) {
                        travelCollection.document(travel.id).set(travel)
                            .addOnSuccessListener {
                                Log.d("MainActivity", "onAvailable: travel inserted to firestore")
                            }
                            .addOnFailureListener {
                                Log.d("MainActivity", "onAvailable: failed to insert travel to firestore")
                            }
                    }
                }
            }
        })
    }
}