package com.sgaikar.finboxassignment.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sgaikar.finboxassignment.data.entities.LocationObj

@Dao
interface LocationDao {

    @Query("SELECT * FROM UserLocations")
    fun getAllLocations() : LiveData<MutableList<LocationObj>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<LocationObj>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationObj): Long


}