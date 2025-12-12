package com.example.microhabits.models.classes

import java.time.LocalDateTime

class CompletedGoal (
    val goalId: Int?,
    var dateCompleted: LocalDateTime? = null
) {
}