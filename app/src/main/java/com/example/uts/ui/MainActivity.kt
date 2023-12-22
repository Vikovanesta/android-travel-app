package com.example.uts.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.uts.R
import com.example.uts.database.TravelDatabase
import com.example.uts.databinding.ActivityMainBinding
import com.example.uts.utils.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager.getInstance(this)

        val db = Room.databaseBuilder(
            applicationContext,
            TravelDatabase::class.java, "travel-database"
        ).allowMainThreadQueries().build()

        setupBottomNav()
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
}