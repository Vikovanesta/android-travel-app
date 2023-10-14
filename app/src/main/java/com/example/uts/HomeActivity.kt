package com.example.uts

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
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

//            if (result.resultCode == Activity.RESULT_OK) {
//                val data = result.data
//                val name = data?.getStringExtra(EXTRA_NAME)
//                val address = data?.getStringExtra(EXTRA_ADDRESS)
//
//                binding.txtName.text = "Hello $name di $address"
//            }
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
        }
    }
}