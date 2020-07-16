package com.example.compass.util.providers

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.compass.util.ALPHA
import com.example.compass.util.to360Degrees

class CompassSensorProvider(context: Context) : SensorEventListener {
    private val appContext = context.applicationContext
    private val sensorManager = appContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var gravityArray: FloatArray = FloatArray(3)
    private var magneticArray: FloatArray = FloatArray(3)
    private val _currentHeading = MutableLiveData<Int>()
    val currentHeading: LiveData<Int>
        get() = _currentHeading

    init {
        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )

        sensorManager.registerListener(
            this,
            sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    private fun lowPass(input: FloatArray, output: FloatArray?): FloatArray {
        if (output == null) return input

        for (i in input.indices) {
            output[i] = output[i] + ALPHA * (input[i].minus(output[i]))
        }

        return output
    }

    fun unregisterListeners() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            gravityArray = lowPass(event.values.clone(), gravityArray)
        }

        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticArray = lowPass(event.values.clone(), magneticArray)
        }


        val R = FloatArray(9)
        val I = FloatArray(9)
        val success = SensorManager.getRotationMatrix(R, I, gravityArray, magneticArray)
        if (success) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(R, orientation)
            _currentHeading.postValue(orientation[0].to360Degrees())
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}