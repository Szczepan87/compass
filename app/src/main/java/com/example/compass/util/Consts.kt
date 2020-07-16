package com.example.compass.util

import android.location.Location
import android.location.LocationManager

const val LOCATION_COMPARISON_THRESHOLD = 0.03
val DEFAULT_LOCATION = Location(LocationManager.GPS_PROVIDER).apply {
    longitude = 0.0
    latitude = 0.0
}
const val ALPHA = 0.15f
const val COORDINATES_DIALOG_TAG = "LongLat Dialog"
const val REQUEST_PERMISSION_CODE = 1