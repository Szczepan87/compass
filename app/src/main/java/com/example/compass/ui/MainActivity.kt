package com.example.compass.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.compass.R
import com.example.compass.databinding.ActivityMainBinding
import com.example.compass.util.COORDINATES_DIALOG_TAG
import com.example.compass.util.REQUEST_PERMISSION_CODE
import com.example.compass.util.toPositiveDegrees
import com.example.compass.vm.CompassActivityViewModel
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var compassActivityViewModel: CompassActivityViewModel = get()
    private val destinationLocationObserver = Observer<Location?> {
        compassActivityViewModel.updateCurrentLocation()
        val currentAzimuth: Float? =
            compassActivityViewModel.currentLocation.value?.bearingTo(it)
        compassActivityViewModel.updateCurrentAzimuth(
            currentAzimuth?.toPositiveDegrees()
        )
    }
    private val currentLocationObserver = Observer<Location?> {
        val destinationLocation: Location? = compassActivityViewModel.destinationLocation.value
        if (destinationLocation == null) {
            compassActivityViewModel.updateCurrentAzimuth(null)
        } else {
            val currentAzimuth: Float? = it?.bearingTo(destinationLocation)
            compassActivityViewModel.updateCurrentAzimuth(currentAzimuth?.toPositiveDegrees())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        binding.lifecycleOwner = this
        binding.viewModel = compassActivityViewModel

        handlePermissions()

        compassActivityViewModel.updateCurrentLocation()

        compassActivityViewModel.destinationLocation.observe(this, destinationLocationObserver)

        compassActivityViewModel.currentLocation.observe(this, currentLocationObserver)

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
