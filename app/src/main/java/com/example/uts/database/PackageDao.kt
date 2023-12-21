package com.example.uts.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.uts.model.TravelPackage

@Dao
interface PackageDao {
    @Upsert
    fun upsertPackage(vararg travelPackages: TravelPackage)

    @Delete
    fun deletePackage(vararg travelPackages: TravelPackage)

    @Query("SELECT * FROM travel_packages")
    fun getAllPackages(): List<TravelPackage>

    @Query("SELECT * FROM travel_packages WHERE id = :id")
    fun getPackageById(id: String): TravelPackage

    @Query("SELECT * FROM travel_packages WHERE name LIKE :name")
    fun getPackageByName(name: String): TravelPackage
}