package com.example.uts.database

import androidx.room.TypeConverter
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Converters {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    @TypeConverter
    fun fromDate(date: Date?): String? {
        return date?.let { dateFormat.format(it) }
    }

    @TypeConverter
    fun toDate(dateString: String?): Date? {
        return dateString?.let { dateFormat.parse(it) }
    }

    @TypeConverter
    fun fromTime(time: Time?): String? {
        return time?.let { timeFormat.format(it) }
    }

    @TypeConverter
    fun toTime(timeString: String?): Time? {
        return timeString?.let { timeFormat.parse(it)?.let { it1 -> Time(it1.time) } }
    }
}