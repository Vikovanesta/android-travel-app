package com.example.uts.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.lang.Package

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
    var departureDate: String = "",
    var departureTime: String = "",
    var arrivalDate: String = "",
    var arrivalTime: String = "",
    var duration: String = "",
    var price: Int = 0,
)

@Entity(
    tableName = "travel_package_cross_ref",
    primaryKeys = ["travelId", "packageId"],
    foreignKeys = [
        ForeignKey(entity = Travel::class, parentColumns = ["id"], childColumns = ["travelId"]),
        ForeignKey(entity = Package::class, parentColumns = ["packageId"], childColumns = ["packageId"])
    ]
)
data class TravelPackageCrossRef(
    val travelId: Int,
    val packageId: Int,
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

    @Relation(
        parentColumn = "id",
        entityColumn = "packageId",
        associateBy = Junction(TravelPackageCrossRef::class)
    )
    val packages: List<Package>
)
