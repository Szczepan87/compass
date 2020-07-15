package com.example.compass.util

import android.location.Location
import android.location.LocationManager

const val LOCATION_COMPARISON_THRESHOLD = 0.03
val DEFAULT_LOCATION = Location(LocationManager.GPS_PROVIDER).apply {
    longitude = 0.0
    latitude = 0.0
}