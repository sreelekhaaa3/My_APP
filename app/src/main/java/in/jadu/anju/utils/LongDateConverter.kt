package `in`.jadu.anju.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertLongDate(date: Long): String {
    val dateProcess = Date(date * 1000L) // Convert Unix timestamp to Date object
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) // Define date format
    return sdf.format(dateProcess)
}

fun getTodayDate(): Long {
    val currentTimeMillis =
        System.currentTimeMillis() // Current time in milliseconds since January 1, 1970, 00:00:00 UTC
    return currentTimeMillis / 1000L
}