package com.example.uts

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
                val travelPackages = data?.getStringArrayExtra(InputTravelPlanActivity.EXTRA_TRAVEL_PACKAGES)
                val totalPrice = data?.getIntExtra(InputTravelPlanActivity.EXTRA_TRAVEL_PRICE, 0)

                with(binding) {
                    textViewTrainClass.text = ": $trainClass"
                    textViewTravelDeparture.text = ": $trainDeparture"
                    textViewTravelDestination.text = ": $trainDestination"
                    textViewTravelDate.text = ": $travelDate"
                    textViewTravelPrice.text = ": Rp$totalPrice,00"

                    textViewNoTravelPlan.visibility = ViewGroup.GONE
                    travelPlanCard.visibility = ViewGroup.VISIBLE
                    checkTravelSchedule.visibility = ViewGroup.VISIBLE

                    binding.travelPackages.removeAllViews()

                    if (travelPackages.isNullOrEmpty()) {
                        val textView = TextView(this@HomeActivity)
                        textView.apply {
                            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                            text = "No travel packages selected"
                        }
                        binding.travelPackages.addView(textView)
                    }

                    travelPackages?.forEach { trainPackage ->
                        val androidChip = com.google.android.material.chip.Chip(this@HomeActivity)
                        androidChip.apply {
                            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                            text = trainPackage
                        }
                        binding.travelPackages.addView(androidChip)
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            btnAddTravelPlan.setOnClickListener{
                val intentToInputTravelPlanActivity =
                    Intent(
                        this@HomeActivity,
                        InputTravelPlanActivity::class.java
                    )
                launcher.launch(intentToInputTravelPlanActivity)
            }



            datePicker.init(
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth
            ) {_, p1, p2, p3 ->
                val travelDate = textViewTravelDate.text.toString().replace(": ", "")
                val selectedDate: String =
                    if (p3 < 10 && p2 < 9) {
                        "0$p3/0${p2 + 1}/$p1"
                    } else if (p3 < 10) {
                        "0$p3/${p2 + 1}/$p1"
                    } else if (p2 < 9) {
                        "$p3/0${p2 + 1}/$p1"
                    } else {
                        "$p3/${p2 + 1}/$p1"
                    }
                if (selectedDate == travelDate) {
                    Toast.makeText(
                        this@HomeActivity,
                        "There is a travel plan scheduled on $selectedDate", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@HomeActivity,
                        "No travel plan scheduled on $selectedDate", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}