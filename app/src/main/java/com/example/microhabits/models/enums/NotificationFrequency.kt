package com.example.microhabits.models.enums

enum class NotificationFrequency(val value: String) {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly");

    companion object {
        fun fromInput(input: String): NotificationFrequency {
            return when (input.lowercase()) {
                "day", "daily" -> DAILY
                "week", "weekly" -> WEEKLY
                "month", "monthly" -> MONTHLY
                else -> throw IllegalArgumentException("Invalid frequency: $input")
            }
        }
    }
}