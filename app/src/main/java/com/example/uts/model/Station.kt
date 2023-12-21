package com.example.uts.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stations")
data class Station(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var name: String = "",
    var code: String = "",
    var regency: String = "",
    var province: String = "",
)
