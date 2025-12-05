package com.example.microhabits.models.classes

import org.json.JSONObject

class UserBehaviorWithBehavior(
    val id: Int? = null,
    val userBehavior: UserBehavior,
    val behavior: Behavior
) {
    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("user_behavior", userBehavior.toJson())
            put("behavior", behavior.toJson())
        }
    }
}