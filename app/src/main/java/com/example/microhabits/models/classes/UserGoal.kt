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
    fun toGoal(): Goal {
        return Goal(
            id = this.id,
            name = this.name,
            description = this.description,
            deadline = this.deadline,
            category = this.category
        )
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to this.id,
            "goal_id" to this.goalId,
            "name" to this.name,
            "description" to this.description,
            "deadline" to this.deadline,
            "category" to this.category
        )
    }
}