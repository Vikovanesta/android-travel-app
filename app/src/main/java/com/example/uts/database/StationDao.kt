package com.example.uts.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.uts.model.Station

@Dao
interface StationDao {
    @Upsert
    fun upsertStation(vararg stations: Station)

    @Delete
    fun deleteStation(vararg stations: Station)

    @Query("SELECT * FROM stations")
    fun getAllStations(): LiveData<MutableList<Station>>

    @Query("SELECT * FROM stations WHERE id = :id")
    fun getStationById(id: String): Station

    @Query("SELECT * FROM stations WHERE name LIKE :name")
    fun getStationByName(name: String): Station

    @Query("SELECT * FROM stations WHERE code LIKE :code")
    fun getStationByCode(code: String): Station

    @Query("SELECT * FROM stations WHERE regency LIKE :regency")
    fun getStationByRegency(regency: String): Station

    @Query("SELECT * FROM stations WHERE province LIKE :province")
    fun getStationByProvince(province: String): Station
}