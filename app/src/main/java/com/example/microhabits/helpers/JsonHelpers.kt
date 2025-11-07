package com.example.microhabits.helpers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.microhabits.models.Behavior
import com.example.microhabits.models.UserBehavior
import com.example.microhabits.models.UserBehaviorWithBehavior
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
fun JSONObject.toUserBehavior(): UserBehavior {
    return UserBehavior(
        id = this.optInt("id"),
        goldenBehavior = this.optBoolean("golden_behavior"),
        oldBehavior = this.optBoolean("old_behavior"),
        progress = this.optInt("progress"),
        timeS = this.optLong("time_s"),
        impactSliderValue = this.optDouble("impactSliderValue", 1.0).toFloat(),
        feasibilitySliderValue = this.optDouble("impactSliderValue", 1.0).toFloat(),
        notification = this.optBoolean("notification"),
        completedToday = this.optBoolean("completed_today"),
        notificationFrequency = this.optInt("notification_frequency"),
        notificationInterval = this.optInt("notification_interval"),
        notificationDay = try {
            DayOfWeek.valueOf(this.optString("notification_day", "MONDAY"))
        } catch (e: Exception) {
            DayOfWeek.MONDAY
        },
        notificationTimeOfDay = try {
            LocalTime.parse(this.optString("notification_time_of_day", "00:00"))
        } catch (e: Exception) {
            LocalTime.MIDNIGHT
        },
        startDate = try {
            LocalDate.parse(this.optString("start_date", LocalDate.now().toString()))
        } catch (e: Exception) {
            LocalDate.now()
        },
        behaviorId = this.optInt("behavior_id"),
        userId = this.optInt("user_id"),
        goalId = this.optInt("goal_id"),
        isAdded = this.optBoolean("isAdded")
    )
}

fun JSONObject.toBehavior(completedToday: Boolean = false): Behavior {
    return Behavior(
        id = this.optInt("id"),
        name = this.optString("name"),
        description = this.optString("description"),
        measuredIn = this.optString("measured_in"),
        categoryId = this.optInt("category_id"),
        completedToday = completedToday
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun JSONObject.toUserBehaviorWithBehavior(): UserBehaviorWithBehavior {
    val id = this.optInt("id")
    val userBehavior = this.getJSONObject("user_behavior").toUserBehavior()
    val behavior = this.getJSONObject("behavior").toBehavior(userBehavior.completedToday)
    return UserBehaviorWithBehavior(id, userBehavior, behavior)
}
