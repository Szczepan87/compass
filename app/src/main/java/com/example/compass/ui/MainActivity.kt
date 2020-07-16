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
import androidx.lifecycle.Observer
import com.example.compass.R
import com.example.compass.databinding.ActivityMainBinding
import com.example.compass.util.COORDINATES_DIALOG_TAG
import com.example.compass.util.REQUEST_PERMISSION_CODE
import com.example.compass.util.to360Degrees
import com.example.compass.util.toPositiveDegrees
import com.example.compass.vm.CompassActivityViewModel
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var compassActivityViewModel: CompassActivityViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        binding.lifecycleOwner = this
        binding.viewModel = compassActivityViewModel
        compassActivityViewModel.updateCurrentLocation()

        compassActivityViewModel.destinationLocation.observe(
            this,
            Observer {
                compassActivityViewModel.updateCurrentLocation()
                val currentAzimuth = compassActivityViewModel.currentLocation.value?.bearingTo(it)
                compassActivityViewModel.updateCurrentAzimuth(
                    currentAzimuth?.toPositiveDegrees() ?: 0
                )
            })

        handlePermissions()

        binding.coordinatesButton.setOnClickListener {
            LocationProviderDialog(compassActivityViewModel).show(
                supportFragmentManager,
                COORDINATES_DIALOG_TAG
            )
        }
    }

    private fun handlePermissions() {
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
                REQUEST_PERMISSION_CODE
            )
        }
    }
}
