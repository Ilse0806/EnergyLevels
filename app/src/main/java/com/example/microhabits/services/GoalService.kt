package com.example.microhabits.services

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.helpers.toBehavior
import com.example.microhabits.helpers.toUserBehavior
import com.example.microhabits.models.UserBehaviorWithBehavior
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.O)
object GoalService {
    private val behaviorIds = mutableListOf<Int>()

    fun loadGoals(context: Context, goalId: Int) {
        DatabaseService.getRow(
            "user_behavior", mapOf("goal_id" to goalId, "fetch_one" to false), context,
            { detailsBehaviorResponse ->
                saveUserBehaviors(detailsBehaviorResponse)

                DatabaseService.getRow(
                    "behavior", mapOf("id" to behaviorIds, "fetch_one" to false), context,
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
            val behaviorJson = rows.getJSONObject(i)
            val behaviorId = behaviorJson["behavior_id"] as Int
            behaviorIds.add(behaviorId)

            val userBehavior = behaviorJson.toUserBehavior()

            val exists = VariableModel.detailsBehaviors.any { it.id == userBehavior.id }

            if (!exists) {
                VariableModel.detailsBehaviors.add(userBehavior)
            }
        }
    }

    private fun saveBehaviorsAccordingly(response: JSONObject) {
        val allBehaviors = response.getJSONArray("rows")
        for (i in 0 until allBehaviors.length()) {
            val behaviorJson = allBehaviors.get(i) as JSONObject
            val behavior = behaviorJson.toBehavior()

            val exists = VariableModel.connectedBehaviors.any { it.id == behavior.id }
            if (!exists) {
                VariableModel.connectedBehaviors.add(behavior)
            }
        }

        val habitsById = VariableModel.connectedBehaviors.associateBy { it.id }
        VariableModel.detailsBehaviors.forEach { userBehavior ->
            val behavior = habitsById[userBehavior.behaviorId]
            behavior?.let {
                val combined = UserBehaviorWithBehavior(null, userBehavior, it)
                VariableModel.combinedBehaviors.add(combined)
            }
        }
    }
}