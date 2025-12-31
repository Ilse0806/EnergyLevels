package com.example.microhabits.models.classes

import com.example.microhabits.models.enums.EnergyLevels
import java.time.LocalDate

class EnergyLevel(
    var date: LocalDate,
    var level: EnergyLevels = EnergyLevels.UNKNOWN,
    var reason: String,
) {
}