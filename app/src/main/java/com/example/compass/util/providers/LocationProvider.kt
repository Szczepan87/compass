package com.example.compass.util.providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.compass.util.DEFAULT_LOCATION
import com.example.compass.util.LOCATION_COMPARISON_THRESHOLD
import kotlin.math.abs

class LocationProvider(context: Context) {

    private val appContext = context.applicationContext
    private val locationManager: LocationManager =
        appContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val _currentLocation = MutableLiveData<Location>()
    val currentLocation: LiveData<Location>
        get() = _currentLocation

    fun updateCurrentLocation() {
        _currentLocation.postValue(getLastKnownLocation())
    }

    fun hasLocationChanged(): Boolean {
        val lastLocation = currentLocation.value
        val mostRecentLocation = getLastKnownLocation()

        return (abs(
            mostRecentLocation.latitude.minus(
                lastLocation?.latitude ?: 0.0
            )
        ) > LOCATION_COMPARISON_THRESHOLD
                && abs(
            mostRecentLocation.longitude.minus(
                lastLocation?.longitude ?: 0.0
            )
        ) > LOCATION_COMPARISON_THRESHOLD
                )
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location {
        return if (hasLocationPermission()) {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                ?: DEFAULT_LOCATION
        } else DEFAULT_LOCATION
    }

    private fun hasLocationPermission(): Boolean = ContextCompat.checkSelfPermission(
        appContext,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                appContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}