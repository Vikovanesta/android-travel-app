package com.example.uts.ui.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.example.uts.adapter.SpinnerStationAdapter
import com.example.uts.databinding.FragmentCreateTravelBinding
import com.example.uts.model.Station
import com.example.uts.model.Travel
import com.example.uts.utils.SessionManager
import com.google.firebase.firestore.FirebaseFirestore

class CreateTravelFragment : Fragment(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener
{
    private var _binding: FragmentCreateTravelBinding? = null
    private val binding get() = _binding!!
    private val firestore = FirebaseFirestore.getInstance()
    private val stationCollection = firestore.collection("stations")
    private val travelCollection = firestore.collection("travels")
    private var dateContext = "departure"
    private var timeContext = "departure"
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTravelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager.getInstance(requireContext())
        setupSpinner()
        setupDateTimeButton()
        setupAddButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        when (dateContext) {
            "departure" -> {
                binding.inputDepartureDate.setText("$year-${month + 1}-$dayOfMonth")
            }
            "arrival" -> {
                binding.inputArrivalDate.setText("$year-${month + 1}-$dayOfMonth")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val formattedHour = String.format("%02d", hourOfDay)
        val formattedMinute = String.format("%02d", minute)
        when (timeContext) {
            "departure" -> {
                binding.inputDepartureTime.setText("$formattedHour:$formattedMinute")
            }
            "arrival" -> {
                binding.inputArrivalTime.setText("$formattedHour:$formattedMinute")
            }
        }
    }

    private fun setupSpinner() {
        val adapterWagonClass = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(com.example.uts.R.array.wagon_classes)
        )
        binding.spinnerWagonClass.adapter = adapterWagonClass

        val stations = mutableListOf<Station>()
        stationCollection.orderBy("name").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val station = document.toObject(Station::class.java)
                stations.add(station)
            }
            val adapterStation = SpinnerStationAdapter(
                requireContext(),
                stations
            )
            binding.spinnerOriginStation.adapter = adapterStation
            binding.spinnerArrivalStation.adapter = adapterStation
        }.addOnFailureListener { exception ->
            exception.printStackTrace()
        }
    }

    private fun setupDateTimeButton() {
        with(binding) {
            btnShowDatePickerDeparture.setOnClickListener {
                dateContext = "departure"
                val datePicker = com.example.uts.ui.dialogs.DatePicker()
                datePicker.setOnDateListener(this@CreateTravelFragment)
                datePicker.show(parentFragmentManager, "date picker")
            }

            btnShowTimePickerDeparture.setOnClickListener {
                timeContext = "departure"
                val timePicker = com.example.uts.ui.dialogs.TimePicker()
                timePicker.setOnTimeListener(this@CreateTravelFragment)
                timePicker.show(parentFragmentManager, "time picker")
            }

            btnShowDatePickerArrival.setOnClickListener {
                dateContext = "arrival"
                val datePicker = com.example.uts.ui.dialogs.DatePicker()
                datePicker.setOnDateListener(this@CreateTravelFragment)
                datePicker.show(parentFragmentManager, "date picker")
            }

            btnShowTimePickerArrival.setOnClickListener {
                timeContext = "arrival"
                val timePicker = com.example.uts.ui.dialogs.TimePicker()
                timePicker.setOnTimeListener(this@CreateTravelFragment)
                timePicker.show(parentFragmentManager, "time picker")
            }
        }
    }

    private fun setupAddButton() {
        with(binding) {
            btnAdd.setOnClickListener {
                val originStation = spinnerOriginStation.selectedItem as Station
                val arrivalStation = spinnerArrivalStation.selectedItem as Station

                //validate
                if (inputTrainName.text.toString().isEmpty()) {
                    inputTrainName.error = "Train name is required"
                    inputTrainName.requestFocus()
                    return@setOnClickListener
                }
                if (inputTrainNumber.text.toString().isEmpty()) {
                    inputTrainNumber.error = "Train number is required"
                    inputTrainNumber.requestFocus()
                    return@setOnClickListener
                }
                if (inputWagonSubClass.text.toString().isEmpty()) {
                    inputWagonSubClass.error = "Wagon sub class is required"
                    inputWagonSubClass.requestFocus()
                    return@setOnClickListener
                }
                if (inputDepartureDate.text.toString().isEmpty()) {
                    inputDepartureDate.error = "Departure date is required"
                    inputDepartureDate.requestFocus()
                    return@setOnClickListener
                }
                if (inputDepartureTime.text.toString().isEmpty()) {
                    inputDepartureTime.error = "Departure time is required"
                    inputDepartureTime.requestFocus()
                    return@setOnClickListener
                }
                if (inputArrivalDate.text.toString().isEmpty()) {
                    inputArrivalDate.error = "Arrival date is required"
                    inputArrivalDate.requestFocus()
                    return@setOnClickListener
                }
                if (inputArrivalTime.text.toString().isEmpty()) {
                    inputArrivalTime.error = "Arrival time is required"
                    inputArrivalTime.requestFocus()
                    return@setOnClickListener
                }
                if (inputPrice.text.toString().isEmpty()) {
                    inputPrice.error = "Price is required"
                    inputPrice.requestFocus()
                    return@setOnClickListener
                }
                if (spinnerArrivalStation.selectedItemPosition == spinnerOriginStation.selectedItemPosition) {
                    Toast.makeText(requireContext(), "Origin and arrival station cannot be the same", Toast.LENGTH_SHORT).show()
                    spinnerArrivalStation.requestFocus()
                    return@setOnClickListener
                }

                val travel = hashMapOf(
                    "trainName" to inputTrainName.text.toString(),
                    "trainNumber" to inputTrainNumber.text.toString(),
                    "wagonClass" to spinnerWagonClass.selectedItem.toString(),
                    "subClass" to inputWagonSubClass.text.toString(),
                    "originStationId" to originStation.id,
                    "arrivalStationId" to arrivalStation.id,
                    "departureDate" to inputDepartureDate.text.toString(),
                    "departureTime" to inputDepartureTime.text.toString() + ":00",
                    "arrivalDate" to inputArrivalDate.text.toString(),
                    "arrivalTime" to inputArrivalTime.text.toString() + ":00",
                    "duration" to 0,
                    "price" to inputPrice.text.toString().toInt()
                )

                travelCollection.add(travel).addOnSuccessListener {
                    travelCollection.document(it.id).update("id", it.id)
                }.addOnSuccessListener {
                    parentFragmentManager.popBackStack()
                }.addOnFailureListener { exception ->
                    exception.printStackTrace()
                }
            }
        }
    }


}