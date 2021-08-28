package com.sgaikar.finboxassignment.data.repository

import com.sgaikar.finboxassignment.data.entities.LocationObj
import com.sgaikar.finboxassignment.data.local.LocationDao
import javax.inject.Inject

class LocationRepository @Inject constructor(private val localDataSource: LocationDao) {

    fun getLocations() = localDataSource.getAllLocations()

    suspend fun saveLocation(location: LocationObj) = localDataSource.insert(location)
}