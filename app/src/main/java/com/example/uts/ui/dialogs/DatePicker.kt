package com.example.uts.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DatePicker : DialogFragment() {
    private var onDateSetListener: DatePickerDialog.OnDateSetListener? = null

    fun setOnDateListener(listener: DatePickerDialog.OnDateSetListener) {
        onDateSetListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val monthOfYear = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(
            requireContext(),
            onDateSetListener,
            year,
            monthOfYear,
            dayOfMonth)
    }
}