package com.example.compass.vm

import android.content.SharedPreferences
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.compass.CompassSensorProvider
import com.example.compass.LocationProvider
import com.example.compass.util.DEFAULT_LOCATION

class CompassActivityViewModel(
    private val compassSensorProvider: CompassSensorProvider,
    private val locationProvider: LocationProvider,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    init {
        locationProvider.currentLocation.observeForever {
            _currentLocation.postValue(it)
            _currentAzimuth.postValue(
                it.bearingTo(destinationLocation.value ?: DEFAULT_LOCATION).toInt()
            )
        }
        compassSensorProvider.currentHeading.observeForever { _currentHeading.postValue(it) }
    }

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location>
        get() = _currentLocation

    private val _destinationLocation = MutableLiveData<Location>()
    val destinationLocation: LiveData<Location>
        get() = _destinationLocation

    private val _currentHeading = MutableLiveData<Int>()
    val currentHeading: LiveData<Int>
        get() = _currentHeading

    private val _currentAzimuth = MutableLiveData<Int>()
    val currentAzimuth: LiveData<Int>
        get() = _currentAzimuth

    fun updateCurrentLocation() {
        if (locationProvider.hasLocationChanged()) locationProvider.updateCurrentLocation()
    }

    fun updateDestinationLocation(location: Location) {
        _destinationLocation.postValue(location)
    }
}