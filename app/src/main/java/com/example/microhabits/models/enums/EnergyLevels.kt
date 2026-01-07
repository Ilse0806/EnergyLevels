package com.example.microhabits.models.enums

enum class EnergyLevels(val value: String) {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low"),
    UNKNOWN("Unknown");

    companion object {
        fun fromInput(input: String): EnergyLevels {
            return when (input.lowercase()) {
                "high" -> HIGH
                "medium" -> MEDIUM
                "low" -> LOW
                "unknown" -> UNKNOWN
                else -> throw IllegalArgumentException("Invalid frequency: $input")
            }
        }

        fun fromValue(value: Float): EnergyLevels {
            return when {
                value <= 20f -> LOW
                value <= 60f -> MEDIUM
                value <= 100f -> HIGH
                else -> UNKNOWN
            }
        }
    }
}