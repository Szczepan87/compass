package com.example.compass.util

fun Float.toPositiveDegrees(): Int {
    return if (this < 0.0) {
        this.plus(360).toInt()
    } else {
        this.toInt()
    }
}

fun Number.to360Degrees(): Int {
    return Math.toDegrees(this.toDouble()).plus(360).rem(360).toInt()
}