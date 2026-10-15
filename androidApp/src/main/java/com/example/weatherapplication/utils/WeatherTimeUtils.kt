package com.example.weatherapplication.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.SimpleTimeZone

object WeatherTimeUtils {

    /**
     * Formats time (seconds) into "h:mm a" using the provided timezone offset (seconds).
     * Compatible with API 24.
     */
    fun getTime(timeSeconds: Long, timezoneOffsetSeconds: Int): String {
        val date = Date(timeSeconds * 1000L)
        val sdf = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        sdf.timeZone = SimpleTimeZone(timezoneOffsetSeconds * 1000, "local")
        return sdf.format(date)
    }

    /**
     * Formats time (seconds) into "MMM dd, h:mm a" using the provided timezone offset (seconds).
     * Compatible with API 24.
     */
    fun getFormattedDate(timeSeconds: Long, timezoneOffsetSeconds: Int): String {
        val date = Date(timeSeconds * 1000L)
        val sdf = SimpleDateFormat("MMM dd, h:mm a", Locale.ENGLISH)
        sdf.timeZone = SimpleTimeZone(timezoneOffsetSeconds * 1000, "local")
        return sdf.format(date)
    }

    /**
     * Determines if it is currently day based on the provided timestamps.
     */
    fun isDay(currentTimeSeconds: Long, sunriseSeconds: Double, sunsetSeconds: Double): Boolean {
        return currentTimeSeconds.toDouble() in sunriseSeconds..sunsetSeconds
    }
}
