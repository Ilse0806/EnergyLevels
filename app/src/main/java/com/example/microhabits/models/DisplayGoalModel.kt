package com.example.microhabits.models

import android.content.Context
import android.util.Log
import com.example.microhabits.api.DatabaseService
import org.json.JSONObject

object DisplayGoalModel {
    private val behaviors = mutableListOf<Int>()

    fun loadGoals(context: Context, goalId: Int) {
        DatabaseService.getRow(
            "user_behavior", mapOf("goal_id" to goalId, "fetch_one" to false), context,
            { detailsBehaviorResponse ->
                saveUserBehaviors(detailsBehaviorResponse)
                DatabaseService.getRow(
                    "behavior", mapOf("id" to behaviors, "fetch_one" to false), context,
                    { fullBehaviorResponse ->
                        saveBehaviorsAccordingly(fullBehaviorResponse)
                        Log.d("GET_BEHAVIOR_SUCCESSFUL", fullBehaviorResponse.toString())
                    },
                    { error -> Log.e("GET_BEHAVIOR_ERROR", error.toString()) }
                )

                Log.d("GET_USER_BEHAVIOR_SUCCESSFUL", detailsBehaviorResponse.toString())
            },
            { error -> Log.e("GET_USER_BEHAVIOR_ERROR", error.toString()) }
        )
    }

    private fun saveUserBehaviors (response: JSONObject) {
        val rows = response.getJSONArray("rows")
        for (i in 0 until rows.length()) {
            val behaviorId = rows.getJSONObject(i)["behavior_id"] as Int
            behaviors.add(behaviorId)

            val behavior = rows.getJSONObject(i)
            val map = behavior.keys().asSequence().associateWith { key ->
                behavior.opt(key)
            }
            val exists = VariableModel.detailsBehaviors.any { it["id"] == map["id"] }

            if (!exists) {
                VariableModel.detailsBehaviors.add(map)
            }
        }
    }

    private fun saveBehaviorsAccordingly(response: JSONObject) {
        val allBehaviors = response.getJSONArray("rows")
        for (i in 0 until allBehaviors.length()) {
            val r = allBehaviors.get(i) as JSONObject
            val map = r.keys().asSequence().associateWith { key ->
                r.opt(key)
            }

            val exists = VariableModel.connectedBehaviors.any { it["id"] == map["id"] }
            if (!exists) {
                VariableModel.connectedBehaviors.add(map)
            }
        }

        val habitsById = VariableModel.connectedBehaviors.associateBy { it["id"] as Int }
        VariableModel.detailsBehaviors.forEach { behavior ->
            val habitId = behavior["behavior_id"] as Int
            val habit = habitsById[habitId]
            habit?.let {
                val combined = mutableMapOf<String, Any?>()
                combined.putAll(habit)
                combined.putAll(behavior)
                VariableModel.combinedBehaviors.add(combined)
            }
        }
    }
}