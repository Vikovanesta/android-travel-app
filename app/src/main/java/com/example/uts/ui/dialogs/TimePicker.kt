package com.example.uts.ui.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class TimePicker: DialogFragment() {
    private var onTimeSetListener: TimePickerDialog.OnTimeSetListener? = null

    fun setOnTimeListener(listener: TimePickerDialog.OnTimeSetListener) {
        onTimeSetListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return TimePickerDialog(
            requireContext(),
            onTimeSetListener,
            hour,
            minute,
            true)
    }
}