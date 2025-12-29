//package com.example.microhabits.services
//
//import android.content.Context
//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresApi
//import androidx.compose.runtime.mutableStateListOf
//import com.example.microhabits.api.DatabaseService
//import com.example.microhabits.data.state.VariableModel
//import com.example.microhabits.helpers.toBehavior
//import com.example.microhabits.helpers.toUserBehavior
//import com.example.microhabits.models.enums.NotificationFrequency
//import com.example.microhabits.models.classes.UserBehavior
//import com.example.microhabits.models.classes.UserBehaviorWithBehavior
//import org.json.JSONObject
//import java.time.LocalDate
//import java.time.LocalTime
//
//object NewBehaviorService {
//    var exampleBehaviors = mutableStateListOf<UserBehaviorWithBehavior>()
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun loadBehaviorsForCategory(categoryId: Int, context: Context) {
//        DatabaseService.getRow("behavior", mapOf("category_id" to categoryId, "fetch_one" to false), context,
//            { behaviorsByCategory ->
//
//                val behaviorRows = behaviorsByCategory.getJSONArray("rows")
//                for (i in 0 until behaviorRows.length()) {
//                    val behavior = behaviorRows.getJSONObject(i)
//                    DatabaseService.getRow("user_behavior",mapOf("behavior_id" to behavior.getInt("id"), "fetch_one" to true),
//                        context,
//                        { userBehaviors ->
//                            saveExampleBehaviors(behavior, userBehaviors)
//                            Log.d("GET_BEHAVIOR_BY_CATEGORY_SUCCESSFUL",behaviorsByCategory.toString())
//                        },
//                        { error ->
//                            saveSingularBehavior(behavior)
//                            Log.e("GET_BEHAVIOR_BY_CATEGORY_ERROR", error.toString())
//                        }
//                    )
//                }
//
//                Log.d("GET_BEHAVIOR_BY_CATEGORY_SUCCESSFUL", behaviorsByCategory.toString())
//            },
//            { error -> Log.e("GET_BEHAVIOR_BY_CATEGORY_ERROR", error.toString()) }
//        )
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun saveExampleBehaviors(behavior: JSONObject, userBehavior: JSONObject) {
//        if (exampleBehaviors.any { it.behavior.id == behavior.optInt("id") }) return
//
//        val newFullBehavior = UserBehaviorWithBehavior(
//            null,
//            userBehavior.toUserBehavior(),
//            behavior.toBehavior()
//        )
//
//        val newList = exampleBehaviors.apply {
//            add(newFullBehavior)
//        }
//        exampleBehaviors = newList
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    fun saveSingularBehavior(behavior: JSONObject) {
//        if (exampleBehaviors.any { it.behavior.id == behavior.optInt("id") }) return
//
//        val newFullBehavior = UserBehaviorWithBehavior(
//            null,
//            UserBehavior(
//                id = null,
//                goldenBehavior = false,
//                oldBehavior = false,
//                progress = null,
//                timeS = 1,
//                notification = true,
//                completedToday = false,
//                notificationFrequency = NotificationFrequency.DAILY,
//                notificationInterval = null,
//                notificationDay = LocalDate.now().dayOfWeek,
//                notificationTimeOfDay = LocalTime.of(10, 0),
//                startDate = LocalDate.now(),
//                behaviorId = null,
//                userId = VariableModel.userId,
//                goalId = null,
//                isAdded = false
//            ),
//            behavior.toBehavior()
//        )
//
//        val newList = exampleBehaviors.apply {
//            add(newFullBehavior)
//        }
//        exampleBehaviors = newList
//    }
//
//
//    fun calculateGoldenBehavior() {
//        val newGoldenBehavior = VariableModel.selectedBehaviors.filter { item ->
//            val impactValue = item.userBehavior.impactSliderValue
//            val feasibilityValue = item.userBehavior.feasibilitySliderValue
//            impactValue > 5 && feasibilityValue > 5
//        }
//
//        newGoldenBehavior.forEach { item ->
//            if (!VariableModel.goldenBehaviors.contains(item)) {
//                VariableModel.goldenBehaviors.add(item)
//            }
//        }
//    }
//
//    fun sortItems(allItems: List<UserBehaviorWithBehavior>): List<UserBehaviorWithBehavior> {
//        val sortedItems = allItems.sortedWith(
//            compareBy(
//                { it.userBehavior.impactSliderValue },
//                { it.userBehavior.feasibilitySliderValue }
//            )
//        )
//
//        return sortedItems
//    }
//
//    fun saveBehaviorAndGoal(newGoal: String, connectedBehaviors: List<UserBehaviorWithBehavior>, context : Context): Int? {
//        var goalId: Int? = null
//        val latch = java.util.concurrent.CountDownLatch(1)
//        DatabaseService.updateRow("goal", mapOf("name" to newGoal), context,
//            { goalResponse ->
//                goalId = goalResponse.getInt("id")
//
//                DatabaseService.updateRow("user_goal", mapOf("user_id" to VariableModel.userId, "goal_id" to goalId), context,
//                    { goalConnected ->
//                        Log.d("API_SUCCESS_GOAL_CONNECTED", goalConnected.toString())
//
//                    },
//                    { error -> Log.e("API_ERROR", error.toString()) }
//                )
//
//                connectedBehaviors.forEach { behavior ->
//                    DatabaseService.updateRow("behavior", behavior.behavior.toMap(), context,
//                        { behaviorResponse ->
//
//                            val updatedUserBehavior = behavior.userBehavior.toMap().toMutableMap()
//                            updatedUserBehavior["behavior_id"] = behaviorResponse.getString("id")
//                            updatedUserBehavior["goal_id"] = goalId
//                            updatedUserBehavior["user_id"] = VariableModel.userId
//
//                            DatabaseService.updateRow("user_behavior", updatedUserBehavior, context,
//                                { userBehaviorResponse ->
//                                    Log.d("API_SUCCESS_USER_BEHAVIOR_UPDATED", userBehaviorResponse.toString())
//                                },
//                                { error -> Log.e("API_ERROR", error.toString()) }
//                            )
//                            Log.d("API_SUCCESS_BEHAVIOR_UPDATED", behaviorResponse.toString())
//                        },
//                        { error -> Log.e("API_ERROR", error.toString()) }
//                    )
//                }
//
//                Log.d("API_SUCCESS_GOALS_UPDATED", goalResponse.toString())
//            },
//            { error -> Log.e("API_ERROR", error.toString()) }
//        )
//        latch.await()
//        return goalId
//    }
//}