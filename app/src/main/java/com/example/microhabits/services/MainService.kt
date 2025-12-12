package com.example.microhabits.services

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.helpers.toCompletedGoal
import com.example.microhabits.helpers.toUserGoal
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.O)
object MainService {
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

        connectGoals(context, VariableModel.userId)
        FavoritesService.loadFavorites(context, VariableModel.userId)
    }

    private fun connectGoals(context: Context, userId: Int) {
        DatabaseService.getRow("user_goal", mapOf("user_id" to userId, "fetch_one" to false), context,
            { userGoals ->
                val rows = userGoals.getJSONArray("rows")
                for (i in 0 until rows.length()) {
                    val userGoal = rows.getJSONObject(i)
                    val userGoalId = userGoal.get("goal_id") as Int

                    DatabaseService.getRow("user_goal_completed", mapOf("user_goal_id" to userGoalId, "fetch_one" to true), context,
                        { completed ->
                            VariableModel.completedGoals.add(completed.toCompletedGoal())
                            Log.d("GET_COMPLETED_SUCCESSFUL", completed.toString()) },
                        { error ->
                            Log.e("NO_USER_GOAL_COMPLETED_FOUND", error.toString())
                        }
                    )
                    DatabaseService.getRow("goal", mapOf("id" to userGoalId, "fetch_one" to true), context,
                        { goal ->
                            VariableModel.userGoals.add(goal.toUserGoal(userGoalId))
                            Log.d("GET_GOAL_SUCCESSFUL", goal.toString()) },
                        { error -> Log.e("GET_GOAL_ERROR", error.toString()) }
                    )
                }
                Log.d("GET_USER_GOAL_SUCCESSFUL", userGoals.toString()) },
            { error -> Log.e("GET_USER_GOAL_ERROR", error.toString())}
        )
    }
}