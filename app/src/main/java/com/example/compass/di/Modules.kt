package com.example.compass.di

import android.content.Context
import android.content.SharedPreferences
import android.hardware.SensorManager
import android.location.LocationManager
import com.example.compass.R
import com.example.compass.vm.CompassActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sensorsModule = module {
    single { provideSensors(androidContext()) }
    single { provideGPS(androidContext()) }
}

val sharedPrefsModule = module {
    single { provideSharedPrefs(androidContext()) }
}

val viewModelModule = module {
    viewModel {
        CompassActivityViewModel(get(), get(), get())
    }
}

private fun provideSensors(context: Context): SensorManager {
    return context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
}

private fun provideGPS(context: Context): LocationManager {
    return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
}

private fun provideSharedPrefs(context: Context): SharedPreferences {
    return context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
}