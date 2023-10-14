package com.example.uts

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import com.example.uts.databinding.ActivityHomeBinding
import com.example.uts.databinding.ActivityRegisterBinding
import com.google.android.material.datepicker.MaterialDatePicker

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private val launcher =
        registerForActivityResult(ActivityResultContracts.
        StartActivityForResult()) {
                result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val trainClass = data?.getStringExtra(InputTravelPlanActivity.EXTRA_TRAIN_CLASS)
                val trainDeparture = data?.getStringExtra(InputTravelPlanActivity.EXTRA_TRAVEL_DEPARTURE)
                val trainDestination = data?.getStringExtra(InputTravelPlanActivity.EXTRA_TRAVEL_DESTINATION)
                val travelDate = data?.getStringExtra(InputTravelPlanActivity.EXTRA_TRAVEL_DATE)

                with(binding) {
                    textViewTrainClass.text = ": $trainClass"
                    textViewTravelDeparture.text = ": $trainDeparture"
                    textViewTravelDestination.text = ": $trainDestination"
                    textViewTravelDate.text = ": $travelDate"
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            for (i in 1..5) {
                val androidChip = com.google.android.material.chip.Chip(this@HomeActivity)
                androidChip.apply {
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    text = "Package"
                }
                travelPackages.addView(androidChip)
            }

            // Go to input travel plan activity
            btnAddTravelPlan.setOnClickListener{
                val intentToInputTravelPlanActivity =
                    Intent(
                        this@HomeActivity,
                        InputTravelPlanActivity::class.java
                    )
                launcher.launch(intentToInputTravelPlanActivity)
            }
        }
    }
}