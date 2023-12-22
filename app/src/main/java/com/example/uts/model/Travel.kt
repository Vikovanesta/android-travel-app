package com.example.uts.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.uts.utils.DateTimeUtil
import com.google.firebase.firestore.DocumentSnapshot
import java.sql.Time
import java.util.Date

@Entity(tableName = "travels")
data class Travel(
    @PrimaryKey(autoGenerate = false)
    var id: String = "",
    var trainName: String = "",
    var trainNumber: String = "",
    var wagonClass: String = "",
    var subClass: String = "",
    var originStationId: String = "",
    var arrivalStationId: String = "",
    var departureDate: Date = Date(),
    var departureTime: Time = Time(0),
    var arrivalDate: Date = Date(),
    var arrivalTime: Time = Time(0),
    var duration: Int = 0,
    var price: Int = 0,
)

data class TravelWithAllFields(
    @Embedded val travel: Travel,

    @Relation(
        parentColumn = "originStationId",
        entityColumn = "id"
    )
    val originStation: Station,

    @Relation(
        parentColumn = "arrivalStationId",
        entityColumn = "id"
    )
    val arrivalStation: Station,
)

fun DocumentSnapshot.toTravel(): Travel {
    val departureDateStr = getString("departureDate") ?: ""
    val departureTimeStr = getString("departureTime") ?: ""
    val arrivalDateStr = getString("arrivalDate") ?: ""
    val arrivalTimeStr = getString("arrivalTime") ?: ""

    val departureDate = DateTimeUtil.parseStringToDate(departureDateStr)
    val departureTime = DateTimeUtil.parseStringToTime(departureTimeStr)
    val arrivalDate = DateTimeUtil.parseStringToDate(arrivalDateStr)
    val arrivalTime = DateTimeUtil.parseStringToTime(arrivalTimeStr)

    return Travel(
        id = getString("id") ?: "",
        trainName = getString("trainName") ?: "",
        trainNumber = getString("trainNumber") ?: "",
        wagonClass = getString("wagonClass") ?: "",
        subClass = getString("subClass") ?: "",
        originStationId = getString("originStationId") ?: "",
        arrivalStationId = getString("arrivalStationId") ?: "",
        departureDate = departureDate,
        departureTime = departureTime,
        arrivalDate = arrivalDate,
        arrivalTime = arrivalTime,
        duration = getLong("duration")?.toInt() ?: 0,
        price = getLong("price")?.toInt() ?: 0,
    )
}
