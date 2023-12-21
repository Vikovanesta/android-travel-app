package com.example.uts.util

import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtil {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    fun formatDateToString(date: Date): String {
        return SimpleDateFormat("dd MMMM yyyy", Locale.US).format(date)
    }

    fun parseStringToDate(dateString: String): Date {
        return dateFormat.parse(dateString) ?: throw IllegalArgumentException("Invalid date string: $dateString")
    }

    fun formatTimeToString(time: Time): String {
        return SimpleDateFormat("HH:mm", Locale.US).format(time)
    }

    fun parseStringToTime(timeString: String): Time {
        return Time.valueOf(timeString)
    }
}