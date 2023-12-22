package com.example.uts.model

import com.example.uts.utils.DateTimeUtil
import com.google.firebase.firestore.DocumentSnapshot
import java.sql.Time
import java.util.Date

data class TravelOrder(
    var travelOrderId: String,
    var travelId: String,
    var userId: String,
    var date: Date,
    var time: Time,
    var totalPrice: Int,
)

data class TravelOrderWithAllFields(
    val travelOrder: TravelOrder = TravelOrder(
        "", "", "", Date(), Time(0), 0
    ),

    val travel: TravelWithAllFields = TravelWithAllFields(
        travel = Travel(),
        originStation = Station(),
        arrivalStation = Station(),
    ),
)

fun DocumentSnapshot.toTravelOrder(): TravelOrder {
    val dateStr = getString("date") ?: ""
    val timeStr = getString("time") ?: ""

    val date = DateTimeUtil.parseStringToDate(dateStr)
    val time = DateTimeUtil.parseStringToTime(timeStr)

    return TravelOrder(
        travelOrderId = getString("travelOrderId") ?: "",
        travelId = getString("travelId") ?: "",
        userId = getString("userId") ?: "",
        date = date,
        time = time,
        totalPrice = getLong("totalPrice")?.toInt() ?: 0,
    )
}
