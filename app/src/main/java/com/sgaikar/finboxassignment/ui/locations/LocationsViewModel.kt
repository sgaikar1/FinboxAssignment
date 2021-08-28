package com.sgaikar.finboxassignment.ui.locations

import androidx.lifecycle.*
import com.sgaikar.finboxassignment.data.entities.LocationObj
import com.sgaikar.finboxassignment.data.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel(), LifecycleObserver {

    private  val insertedId =  MutableLiveData<Long>()
    private val  error = MutableLiveData<String>()
    var locationFinalList: LiveData<MutableList<LocationObj>> = MutableLiveData<MutableList<LocationObj>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchLocationData(){
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
}
