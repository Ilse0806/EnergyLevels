package com.example.microhabits.models

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.helpers.toBehavior
import com.example.microhabits.helpers.toUserBehavior
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime

object CreateBehaviorModel {
    var exampleBehaviors = mutableStateListOf<UserBehaviorWithBehavior>()
    @RequiresApi(Build.VERSION_CODES.O)
    fun loadBehaviorsForCategory(categoryId: Int, context: Context) {
        DatabaseService.getRow("behavior", mapOf("category_id" to categoryId, "fetch_one" to false), context,
            { behaviorsByCategory ->

                val behaviorRows = behaviorsByCategory.getJSONArray("rows")
                for (i in 0 until behaviorRows.length()) {
                    val behavior = behaviorRows.getJSONObject(i)
                    println(behavior.getInt("id"))
                    DatabaseService.getRow("user_behavior",mapOf("behavior_id" to behavior.getInt("id"), "fetch_one" to true),
                        context,
                        { userBehaviors ->
                            saveExampleBehaviors(behavior, userBehaviors)
                            Log.d("GET_BEHAVIOR_BY_CATEGORY_SUCCESSFUL",behaviorsByCategory.toString())
                        },
                        { error ->
                            saveSingularBehavior(behavior)
                            Log.e("GET_BEHAVIOR_BY_CATEGORY_ERROR", error.toString())
                        }
                    )
                }

                Log.d("GET_BEHAVIOR_BY_CATEGORY_SUCCESSFUL", behaviorsByCategory.toString())
            },
            { error -> Log.e("GET_BEHAVIOR_BY_CATEGORY_ERROR", error.toString()) }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveExampleBehaviors(behavior: JSONObject, userBehavior: JSONObject) {
        if (exampleBehaviors.any { it.behavior.id == behavior.optInt("id") }) return

        val newFullBehavior = UserBehaviorWithBehavior(
            null,
            userBehavior.toUserBehavior(),
            behavior.toBehavior()
        )

        val newList = exampleBehaviors.apply {
            add(newFullBehavior)
        }
        exampleBehaviors = newList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveSingularBehavior(behavior: JSONObject) {
        if (exampleBehaviors.any { it.behavior.id == behavior.optInt("id") }) return

        val newFullBehavior = UserBehaviorWithBehavior(
            null,
            UserBehavior(
                id = null,
                goldenBehavior = false,
                oldBehavior = false,
                progress = null,
                timeS = 1,
                notification = true,
                completedToday = false,
                notificationFrequency = null,
                notificationInterval = null,
                notificationDay = LocalDate.now().dayOfWeek,
                notificationTimeOfDay = LocalTime.of(10, 0),
                startDate = LocalDate.now(),
                behaviorId = null,
                userId = VariableModel.userId,
                goalId = null,
                isAdded = false
            ),
            behavior.toBehavior()
        )

        val newList = exampleBehaviors.apply {
            add(newFullBehavior)
        }
        exampleBehaviors = newList
    }
}