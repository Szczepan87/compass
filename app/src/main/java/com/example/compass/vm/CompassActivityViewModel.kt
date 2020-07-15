package com.example.compass.vm

import android.content.SharedPreferences
import android.hardware.SensorManager
import android.location.LocationManager
import androidx.lifecycle.ViewModel

class CompassActivityViewModel(
    private val sensorManager: SensorManager,
    private val locationManager: LocationManager,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

}