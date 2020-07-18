package com.example.compass.vm

import android.content.SharedPreferences
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.compass.util.*
import com.example.compass.util.providers.CompassSensorProvider
import com.example.compass.util.providers.LocationProvider

class CompassActivityViewModel(
    private val compassSensorProvider: CompassSensorProvider,
    private val locationProvider: LocationProvider,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val currentLocationObserver = Observer<Location?> {
        _currentLocation.postValue(it)
    }

    private val currentHeadingObserver = Observer<Int> { _currentHeading.postValue(it) }

    private val _currentLocation = MutableLiveData<Location?>()
    val currentLocation: LiveData<Location?>
        get() = _currentLocation

    private val _destinationLocation = MutableLiveData<Location?>().apply {
        val savedLocation = getSavedLocation()
        if (savedLocation.latitude != LATITUDE_DEFAULT_DOUBLE && savedLocation.longitude != LONGITUDE_DEFAULT_DOUBLE) {
            postValue(savedLocation)
        } else {
            postValue(null)
        }
    }
    val destinationLocation: LiveData<Location?>
        get() = _destinationLocation

    private val _currentHeading = MutableLiveData<Int>()
    val currentHeading: LiveData<Int>
        get() = _currentHeading

    private val _currentAzimuth = MutableLiveData<Int?>()
    val currentAzimuth: LiveData<Int?>
        get() = _currentAzimuth

    init {
        locationProvider.currentLocation.observeForever(currentLocationObserver)
        compassSensorProvider.currentHeading.observeForever(currentHeadingObserver)
    }

    fun updateCurrentLocation() {
        locationProvider.updateCurrentLocation()
    }

    fun updateDestinationLocation(location: Location?) {
        locationProvider.updateCurrentLocation()
        _destinationLocation.postValue(location)
        destinationLocation.value?.let {
            saveLongLat(it.longitude, it.latitude)
        }
    }

    fun updateCurrentAzimuth(value: Int?) {
        _currentAzimuth.postValue(value)
    }

    private fun saveLongLat(longitude: Double, latitude: Double) {
        sharedPreferences.edit().putFloat(LONGITUDE_KEY, longitude.toFloat()).apply()
        sharedPreferences.edit().putFloat(LATITUDE_KEY, latitude.toFloat()).apply()
    }

    private fun getSavedLocation(): Location {
        return Location(APP_LOCATION_PROVIDER).apply {
            longitude =
                sharedPreferences.getFloat(LONGITUDE_KEY, LONGITUDE_DEFAULT_FLOAT).toDouble()
            latitude = sharedPreferences.getFloat(LATITUDE_KEY, LATITUDE_DEFAULT_FLOAT).toDouble()
        }
    }

    override fun onCleared() {
        compassSensorProvider.unregisterListeners()
        compassSensorProvider.currentHeading.removeObserver(currentHeadingObserver)
        locationProvider.currentLocation.removeObserver(currentLocationObserver)
        super.onCleared()
    }
}