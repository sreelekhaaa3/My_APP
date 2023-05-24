package `in`.jadu.anju.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object AppUtils{
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


    fun stringToUnix(date: String): Long {
        val pattern = "MMMM d, yyyy"
        val timeZone = TimeZone.getTimeZone("UTC")

        val formatter = SimpleDateFormat(pattern, Locale.US)
        formatter.timeZone = timeZone

        val dateFormatter = formatter.parse(date)

        return dateFormatter?.time ?: 0
    }
}
