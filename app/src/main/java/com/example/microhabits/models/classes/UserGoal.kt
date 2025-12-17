package com.example.microhabits.models.classes

import java.time.LocalDateTime

class UserGoal(
    val id: Int?,
    val goalId: Int?,
    var name: String,
    var description: String = "",
    var deadline: LocalDateTime? = null,
    var category: String = "Exercise"
) {
}