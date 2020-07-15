package com.example.compass

import android.app.Application
import com.example.compass.di.sensorsModule
import com.example.compass.di.sharedPrefsModule
import com.example.compass.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CompassApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            this@CompassApp.applicationContext
            modules(sensorsModule, sharedPrefsModule, viewModelModule)
                .androidContext(this@CompassApp.applicationContext)
        }
    }
}