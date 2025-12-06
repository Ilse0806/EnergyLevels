package com.example.microhabits.services

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.helpers.checkNotification
import com.example.microhabits.helpers.toBehavior
import org.json.JSONArray
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.O)
object HomeService {
    fun loadUser(context: Context) {
        DatabaseService.getRow(
            "user", mapOf("id" to "1", "fetch_one" to true), context,
            { user ->
                checkResponse(context, user)
                Log.d("GET_USER_SUCCESSFUL", user.toString())
            },
            { error -> Log.e("GET_USER_ERROR", error.toString()) }
        )
    }

    private fun checkResponse(context: Context, results: JSONObject) {
        VariableModel.user = results
        VariableModel.userName = results["first_name"].toString()
        VariableModel.userId = results["id"] as Int

        connectBehaviors(context, VariableModel.userId)
    }

    private fun connectBehaviors(context: Context, userId: Int) {
        DatabaseService.getRow("user_behavior", mapOf("user_id" to userId, "fetch_one" to false), context,
            { response ->
                saveBehavior(context, response["rows"] as JSONArray)
                Log.d("GET_USER_BEHAVIOR_SUCCESSFUL", response.toString()) },
            { error -> Log.e("GET_USER_BEHAVIOR_ERROR", error.toString())}
        )
    }

    private fun saveBehavior(context: Context, results: JSONArray) {
        for (i in 0 until results.length()) {
            var behavior = results.get(i) as JSONObject
            val today = checkNotification(behavior)
            val completedToday = behavior["completed_today"]

            DatabaseService.getRow("behavior", mapOf("id" to behavior["behavior_id"], "fetch_one" to true), context,
                { response ->
                    behavior = response
                    val map = behavior.keys().asSequence().associateWith { key ->
                        behavior.opt(key)
                    }.toMutableMap().apply {
                        put("completed_today", completedToday)
                    }
                    val exists = VariableModel.allBehaviors.any { it["id"] == map["id"] }

                    if (!exists) {
                        VariableModel.allBehaviors.add(map)
                        if (today) {
                            VariableModel.todayBehaviors.add(behavior.toBehavior(completedToday == 1))
                        }
                    }
                    Log.d("GET_BEHAVIOR_SUCCESSFUL", response.toString()) },
                { error -> Log.e("GET_BEHAVIOR_ERROR", error.toString())}
            )
        }
    }

    fun saveGoals(context: Context) {
        val goalIds = mutableListOf<Int>()
        DatabaseService.getRow("user_goal", mapOf("user_id" to VariableModel.userId, "fetch_one" to false), context,
            { response ->
                val rows = response.getJSONArray("rows")
                for (i in 0 until rows.length()) {
                    val goalId = rows.getJSONObject(i)["goal_id"] as Int
                    goalIds.add(goalId)
                }

                DatabaseService.getRow("goal", mapOf("id" to goalIds, "fetch_one" to false), context,
                    { goalResponse ->
                        val goals = goalResponse.getJSONArray("rows")
                        for (i in 0 until goals.length()) {
                            val r = goals.get(i) as JSONObject
                            val map = r.keys().asSequence().associateWith { key ->
                                r.opt(key)
                            }

                            val exists = VariableModel.userGoals.any { it["id"] == map["id"] }
                            if (!exists) {
                                VariableModel.userGoals.add(map)
                            }
                        }

                        Log.d("GET_GOALS_SUCCESSFUL", goalResponse.toString()) },
                    { error -> Log.e("GET_GOALS_ERROR", error.toString())}
                )
                Log.d("GET_USER_GOAL_SUCCESSFUL", response.toString()) },
            { error -> Log.e("GET_USER_GOAL_ERROR", error.toString())}
        )
    }

}