package com.example.microhabits.helpers

import android.os.Build
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

// Given it's own file, since I will probably use this multiple times
@RequiresApi(Build.VERSION_CODES.O)
fun checkNotification(behavior: JSONObject): Boolean {
    val startDateStr = behavior["start_date"] as String
    val startDate = LocalDate.parse(startDateStr, DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH))

    if (behavior["notification"] == 1) {
        val todayDate = LocalDate.now()
        val daysSinceStart = ChronoUnit.DAYS.between(startDate, todayDate)
        val interval = behavior["notification_interval"] as Int

        when (behavior["notification_frequency"]) {
            "daily" -> {
                if (daysSinceStart % interval == 0L) {
                    return true
                }
            }
            "weekly" -> {
                val notificationDayIndex = behavior["notification_day"] as? Int
                val todayDayIndex = todayDate.dayOfWeek.value % 7
                if (notificationDayIndex != null && todayDayIndex == notificationDayIndex) {
                    return true
                }
            }
            "monthly" -> {
                val notificationDayOfMonth = behavior["notification_day_of_month"] as? Int
                if (notificationDayOfMonth != null && todayDate.dayOfMonth == notificationDayOfMonth) {
                    return true
                }
            }
        }
    }
    return false
}