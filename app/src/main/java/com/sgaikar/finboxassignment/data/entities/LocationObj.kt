package com.sgaikar.finboxassignment.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserLocations")
data class LocationObj(
    @PrimaryKey
    val timeStamp: String,
    val latitude: Double,
    val longitude: Double,
)