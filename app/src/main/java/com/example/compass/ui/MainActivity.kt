package com.example.compass.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.compass.R
import com.example.compass.databinding.ActivityMainBinding
import com.example.compass.util.to360Degrees
import com.example.compass.util.toPositiveDegrees

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var locationManager: LocationManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var accelerometer: Sensor
    private lateinit var magnetometer: Sensor
    private lateinit var gravityList: FloatArray
    private lateinit var magneticList: FloatArray
    private var heading = 0
    private var longDest = 17.0
    private var latDest = 52.0
    private var currentLong = 0.0
    private var currentLat = 0.0
    private lateinit var currentLocation: Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_main
        )
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        }
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) ?:
            Location("").apply {
                longitude = 0.0
                latitude = 0.0
            }
        currentLong = currentLocation.longitude
        currentLat = currentLocation.latitude
    }

    override fun onResume() {
        super.onResume()

        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            gravityList = event.values
        }

        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticList = event.values
        }

        if (this::magneticList.isInitialized && this::gravityList.isInitialized) {
            val R = FloatArray(9)
            val I = FloatArray(9)
            val success = SensorManager.getRotationMatrix(R, I, gravityList, magneticList)
            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(R, orientation)
                heading = orientation[0].to360Degrees()
            }
        }

        binding.heading = heading
        binding.azimuth = currentLocation.bearingTo(Location("").apply {
            latitude = latDest
            longitude = longDest
        }).toPositiveDegrees()
    }
}
