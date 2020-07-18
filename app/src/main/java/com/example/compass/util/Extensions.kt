package com.example.compass.util

import kotlin.math.roundToInt

fun Float.toPositiveDegrees(): Int {
    return if (this < 0.0) {
        this.plus(360).toInt()
    } else {
        this.toInt()
    }
}

fun Float.to360Degrees(): Int {
    return Math.toDegrees(this.toDouble()).plus(360).rem(360).roundToInt()
}