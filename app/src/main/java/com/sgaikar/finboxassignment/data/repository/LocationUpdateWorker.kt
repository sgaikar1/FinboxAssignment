package com.sgaikar.finboxassignment.data.repository

import android.R
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import com.sgaikar.finboxassignment.data.entities.LocationObj
import com.sgaikar.finboxassignment.data.local.LocationDao
import com.sgaikar.finboxassignment.utils.OnLocationReceived
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LocationUpdateWorker constructor(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {

    lateinit var repository: LocationDao
    lateinit var appContext: Context
    override fun doWork(): Result {

        getFLLocation(appContext, object : OnLocationReceived {
            override fun onLocation(location: LocationObj) {
                GlobalScope.launch(Dispatchers.Main) {
                    repository.insert(location)
                    val locationId: Long = repository.insert(location)
                    val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
                        applicationContext, "LOCATION_NOTIFICATION"
                    )
                        .setSmallIcon(R.mipmap.sym_def_app_icon)
                        .setContentTitle("Location Captured")
                        .setContentText("$locationId-$location")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    val notificationManager = NotificationManagerCompat.from(
                        applicationContext
                    )
                    notificationManager.notify(1234, mBuilder.build())

                }
            }
        })
        return Result.success()
    }


    private fun getFLLocation(activity: Context, callback: OnLocationReceived) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null){
                val location = LocationObj(
                    System.currentTimeMillis(),
                    location.latitude,
                    location.longitude
                )
                callback.onLocation(location)

            }
        }
    }
}

