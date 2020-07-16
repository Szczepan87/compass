package com.example.compass.di

import android.content.Context
import android.content.SharedPreferences
import com.example.compass.util.providers.CompassSensorProvider
import com.example.compass.util.providers.LocationProvider
import com.example.compass.R
import com.example.compass.vm.CompassActivityViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sensorsModule = module {
    single {
        CompassSensorProvider(
            androidContext()
        )
    }
    single { LocationProvider(androidContext()) }
}

val sharedPrefsModule = module {
    single { provideSharedPrefs(androidContext()) }
}

val viewModelModule = module {
    viewModel {
        CompassActivityViewModel(get(), get(), get())
    }
}

private fun provideSharedPrefs(context: Context): SharedPreferences {
    return context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
}