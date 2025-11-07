package com.example.microhabits.models

import org.json.JSONObject
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

class UserBehavior(
    val id: Int?,
    var goldenBehavior: Boolean = false,
    var oldBehavior: Boolean = false,
    var progress: Int?,
    var timeS: Long?,
    var impactSliderValue: Float = 1f,
    var feasibilitySliderValue: Float = 1f,
    var notification: Boolean?,
    var completedToday: Boolean = false,
    var notificationFrequency: Int?,
    var notificationInterval: Int?,
    var notificationDay: DayOfWeek,
    var notificationTimeOfDay: LocalTime,
    var startDate: LocalDate,
    var behaviorId: Int?,
    var userId: Int,
    var goalId: Int?,
    var isAdded: Boolean = false,
) {
    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("golden_behavior", goldenBehavior)
            put("old_behavior", oldBehavior)
            put("progress", progress)
            put("time_s", timeS)
            put("impactSliderValue", impactSliderValue)
            put("feasibilitySliderValue", feasibilitySliderValue)
            put("notification", notification)
            put("completed_today", completedToday)
            put("notification_frequency", notificationFrequency)
            put("notification_interval", notificationInterval)
            put("notification_day", notificationDay)
            put("notification_time_of_day", notificationTimeOfDay)
            put("start_date", startDate)
            put("behavior_id", behaviorId)
            put("user_id", userId)
            put("goal_id", goalId)
            put("isAdded", isAdded)
        }
    }
}