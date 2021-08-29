package com.sgaikar.finboxassignment.ui.locations

import androidx.lifecycle.*
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.sgaikar.finboxassignment.data.entities.LocationObj
import com.sgaikar.finboxassignment.data.repository.LocationRepository
import com.sgaikar.finboxassignment.data.repository.LocationUpdateWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel(), LifecycleObserver {

    private val insertedId = MutableLiveData<Long>()
    private val error = MutableLiveData<String>()
    var locationFinalList: LiveData<MutableList<LocationObj>> =
        MutableLiveData<MutableList<LocationObj>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchLocationData() {
        viewModelScope.launch {

            locationFinalList = repository.getLocations()

        }
    }

    fun insertLocationInfo(location: LocationObj) {
        viewModelScope.launch {
            val locationId: Long = repository.saveLocation(location)
            insertedId.postValue(locationId)
        }
    }

    fun fetchLocationFromDevice(){
        val periodicWork = PeriodicWorkRequest.Builder(LocationUpdateWorker::class.java, 1, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance().enqueue(periodicWork)
    }
}
