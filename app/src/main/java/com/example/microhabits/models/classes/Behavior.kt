package com.example.microhabits.models.classes

import com.example.microhabits.models.enums.MeasuredInResult
import org.json.JSONObject

class Behavior(
    val id: Int?,
    var name: String,
    var description: String = "",
    var measuredIn: MeasuredInResult = MeasuredInResult.AMOUNT_OF_TIMES,
    var categoryId: Int,
    var completedToday: Boolean = false
) {
    fun toJson(): JSONObject {
        return JSONObject().apply {
            put("id", id)
            put("name", name)
            put("description", description)
            put("measured_in", measuredIn.value)
            put("category_id", categoryId)
            put("completed_today", completedToday)
        }
    }

    fun toMap(): Map<String, Any> {
        val map = mutableMapOf(
            "name" to name,
            "measured_in" to measuredIn.value,
            "category_id" to categoryId
        )

        println(measuredIn.value)

        id?.let { map["id"] = it}
        description.isNotBlank().let { map["description"] = it }

        return map
    }
}