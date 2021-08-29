package com.sgaikar.finboxassignment.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.sgaikar.finboxassignment.data.local.LocationDao
import com.sgaikar.finboxassignment.data.repository.LocationUpdateWorker

class DaggerWorkerFactory(private val localDataSource: LocationDao) : WorkerFactory() {

    override fun createWorker(appContext: Context, workerClassName: String, workerParameters: WorkerParameters): ListenableWorker? {

        val workerKlass = Class.forName(workerClassName).asSubclass(Worker::class.java)
        val constructor = workerKlass.getDeclaredConstructor(Context::class.java, WorkerParameters::class.java)
        val instance = constructor.newInstance(appContext, workerParameters)

        when (instance) {
            is LocationUpdateWorker -> {
                instance.repository = localDataSource
                instance.appContext = appContext
            }
            // optionally, handle other workers
        }

        return instance
    }
}