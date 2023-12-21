package com.example.uts.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.uts.model.TravelOrder
import com.example.uts.model.TravelOrderWithAllFields

@Dao
interface OrderDao {
    @Upsert
    fun upsertOrder(vararg order: TravelOrder)

    @Delete
    fun deleteOrder(vararg order: TravelOrder)

    @Query("SELECT * FROM travel_orders")
    fun getAllOrders(): List<TravelOrderWithAllFields>

    @Query("SELECT * FROM travel_orders WHERE travelOrderId = :id")
    fun getOrderById(id: String): TravelOrderWithAllFields

    @Query("SELECT * FROM travel_orders WHERE userId = :userId")
    fun getOrderByUserId(userId: String): List<TravelOrderWithAllFields>

    @Query("SELECT * FROM travel_orders WHERE travelId = :travelId")
    fun getOrderByTravelId(travelId: String): List<TravelOrderWithAllFields>
}