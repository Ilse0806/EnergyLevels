package com.example.microhabits.models

import org.json.JSONObject

class Behavior(
    val id: Int?,
    var name: String,
    var description: String = "",
    var measuredIn: String?,
    var categoryId: Int,
    var completedToday: Boolean = false
) {
    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("name", name)
            put("description", description)
            put("measured_in", measuredIn)
            put("category_id", categoryId)
            put("completed_today", completedToday)
        }
    }
}