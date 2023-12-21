package com.example.uts.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "travel_packages")
data class TravelPackage(
    @PrimaryKey(autoGenerate = false)
    var travelPackageId: String = "",
    var name: String = "",
    var price: Int = 0,
)
