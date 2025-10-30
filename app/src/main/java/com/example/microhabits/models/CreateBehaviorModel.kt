package com.example.microhabits.models

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.microhabits.api.DatabaseService
import org.json.JSONObject

object CreateBehaviorModel {
    var exampleBehaviors = mutableStateOf(JSONObject())
    fun loadBehaviorsForCategory(categoryId: Int, context: Context) {
        exampleBehaviors.value = JSONObject()
        DatabaseService.getRow("behavior", mapOf("category_id" to categoryId, "fetch_one" to false), context,
            { behaviorsByCategory ->
                saveExampleBehaviors(behaviorsByCategory)
                Log.d("GET_BEHAVIOR_BY_CATEGORY_SUCCESSFUL", behaviorsByCategory.toString())
            },
            { error -> Log.e("GET_BEHAVIOR_BY_CATEGORY_ERROR", error.toString()) }
        )
    }

    private fun saveExampleBehaviors(response: JSONObject) {
        val categories = response.getJSONArray("rows")
        val newExampleObject = JSONObject()

        for (i in 0 until categories.length()) {
//            Add value to use in front-end
            val behavior = categories.getJSONObject(i)
            behavior.put("isAdded", false)
            newExampleObject.put(i.toString(), behavior)
        }
        exampleBehaviors.value = newExampleObject
    }
}