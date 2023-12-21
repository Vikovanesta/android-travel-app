package com.example.uts.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

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
