package com.example.compass.util

import org.junit.Assert.assertEquals
import org.junit.Test

class ExtensionsTest {

    @Test
    fun `should return 0 when receive 0`() {
        assertEquals(0, 0f.toPositiveDegrees())
    }

    @Test
    fun `should return 180 when receive -180`() {
        assertEquals(180, (-180f).toPositiveDegrees())
    }

    @Test
    fun `should return 180 when receive 180`() {
        assertEquals(180, 180f.toPositiveDegrees())
    }

    @Test
    fun `should return 270 when receive -90`() {
        assertEquals(270, (-90f).toPositiveDegrees())
    }

    @Test
    fun `should return 90 when receive 90`() {
        assertEquals(90, 90f.toPositiveDegrees())
    }

    @Test
    fun `should return 0 when sensor is N`() {
        val sensorNorthward = 0f
        assertEquals(0, sensorNorthward.to360Degrees())
    }

    @Test
    fun `should return 180 when sensor is S`() {
        val sensorSouthward = Math.PI.toFloat()
        assertEquals(180, sensorSouthward.to360Degrees())
    }

    @Test
    fun `should return 90 when sensor is E`() {
        val sensorEastward = Math.PI.div(2).toFloat()
        assertEquals(90, sensorEastward.to360Degrees())
    }

    @Test
    fun `should return 270 when sensor is W`() {
        val sensorWestward = Math.PI.div(-2).toFloat()
        assertEquals(270, sensorWestward.to360Degrees())
    }
}