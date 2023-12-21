package com.example.uts.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.uts.util.DateTimeUtil
import com.google.firebase.firestore.DocumentSnapshot
import java.sql.Time
import java.util.Date

@Entity(
    tableName = "travel_orders",
    foreignKeys = [
        ForeignKey(entity = Travel::class, parentColumns = ["id"], childColumns = ["travelId"]),
//        ForeignKey(entity = TravelPackage::class, parentColumns = ["id"], childColumns = ["travelPackageId"])
    ]
)
data class TravelOrder(
    @PrimaryKey(autoGenerate = false)
    var travelOrderId: String,
    var travelId: String,
    var userId: String,
    var date: Date,
    var time: Time,
    var totalPrice: Int,
)

@Entity(
    tableName = "order_package_cross_ref",
    primaryKeys = ["travelOrderId", "travelPackageId"],
    foreignKeys = [
        ForeignKey(entity = TravelOrder::class, parentColumns = ["travelOrderId"], childColumns = ["travelOrderId"]),
        ForeignKey(entity = TravelPackage::class, parentColumns = ["travelPackageId"], childColumns = ["travelPackageId"])
    ]
)
data class OrderPackageCrossRef(
    val travelOrderId: String,
    val travelPackageId: String,
)

data class TravelOrderWithAllFields(
    @Embedded val travelOrder: TravelOrder,

    @Relation(
        parentColumn = "travelId",
        entityColumn = "id"
    )
    val travel: Travel,

    @Relation(
        parentColumn = "travelOrderId",
        entityColumn = "travelPackageId",
        associateBy = Junction(OrderPackageCrossRef::class)
    )
    val packages: List<TravelPackage>,
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
