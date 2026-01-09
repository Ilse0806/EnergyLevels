package com.example.microhabits.models.classes

import com.example.microhabits.models.enums.EnergyLevels
import java.time.LocalDate

class EnergyLevel(
    var id: Int?,
    var date: LocalDate,
    var level: EnergyLevels = EnergyLevels.UNKNOWN,
    var percentage: Float = 0f,
    var reason: String,
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to this.id,
            "date" to this.date,
            "energy_level" to this.level.toString(),
            "energy_level_percentage" to this.percentage,
            "reason" to this.reason
        )
    }
}