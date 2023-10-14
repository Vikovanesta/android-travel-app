package com.example.uts

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import com.example.uts.databinding.ActivityInputTravelPlanBinding

class InputTravelPlanActivity : AppCompatActivity(),
    DatePickerDialog.OnDateSetListener
{
    private lateinit var binding: ActivityInputTravelPlanBinding
    companion object {
        const val EXTRA_TRAIN_CLASS = "extra_train_class"
        const val EXTRA_TRAVEL_DEPARTURE = "extra_travel_departure"
        const val EXTRA_TRAVEL_DESTINATION = "extra_travel_destination"
        const val EXTRA_TRAVEL_DATE = "extra_travel_date"
        const val EXTRA_TRAVEL_PACKAGES = "extra_travel_packages"
        const val EXTRA_TRAVEL_PRICE = "extra_travel_price"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputTravelPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            btnShowCalendar.setOnClickListener {
                val datePicker = DatePicker()
                datePicker.show(supportFragmentManager, "date picker")
            }

            val adapterTrainClasses = ArrayAdapter<String>(
                this@InputTravelPlanActivity,
                android.R.layout.simple_spinner_item,
                resources.getStringArray(R.array.train_classes)
            )
            spinnerTrainClasses.adapter = adapterTrainClasses

            val adapterTrainStations = ArrayAdapter<String>(
                this@InputTravelPlanActivity,
                android.R.layout.simple_spinner_item,
                resources.getStringArray(R.array.train_stations)
            )
            spinnerTravelDeparture.adapter = adapterTrainStations
            spinnerTravelDestination.adapter = adapterTrainStations

            btnSubmit.setOnClickListener {
                val intent = Intent()
                val selectedTrainClass: String = spinnerTrainClasses.selectedItem.toString()
                val selectedTrainDeparture: String = spinnerTravelDeparture.selectedItem.toString()
                val selectedTrainDestination: String = spinnerTravelDestination.selectedItem.toString()
                val selectedTravelDate: String = editTextTravelDate.text.toString()

                intent.putExtra(EXTRA_TRAIN_CLASS, selectedTrainClass)
                intent.putExtra(EXTRA_TRAVEL_DEPARTURE, selectedTrainDeparture)
                intent.putExtra(EXTRA_TRAVEL_DESTINATION, selectedTrainDestination)
                intent.putExtra(EXTRA_TRAVEL_DATE, selectedTravelDate)

                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
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
        binding.editTextTravelDate.setText(selectedDate)
    }
}