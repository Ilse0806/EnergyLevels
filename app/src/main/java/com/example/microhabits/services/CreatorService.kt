package com.example.microhabits.services

import android.content.Context
import android.util.Log
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.models.classes.ExerciseProgram
import kotlinx.serialization.json.Json
import org.json.JSONArray
import org.json.JSONObject
import kotlin.toString

object CreatorService {
    fun saveNewGoal(newGoal: Map<String, Any?>, context : Context) {
        DatabaseService.updateRow("goal", newGoal, context,
            { goalResponse ->
                val goalId = goalResponse.getInt("id")
                VariableModel.goal.value?.id = goalId

                DatabaseService.updateRow("user_goal", mapOf("user_id" to VariableModel.userId, "goal_id" to goalId), context,
                    { goalConnected ->
                        Log.d("API_SUCCESS_GOAL_CONNECTED", goalConnected.toString())
                    },
                    { error -> Log.e("API_ERROR", error.toString()) }
                )

                Log.d("API_SUCCESS_GOALS_UPDATED", goalResponse.toString())
            },
            { error -> Log.e("API_ERROR", error.toString()) }
        )
    }

    fun updateGoal(updatedGoal: Map<String,Any?>, context: Context) {
        DatabaseService.updateRow("goal", updatedGoal, context,
            { response ->
                Log.d("API_GOAL_UPDATED", response.toString())
            },
            { error -> Log.e("API_ERROR", error.toString())}
        )
    }

    fun saveNewExercise (newExercise: ExerciseProgram, context: Context) {
        val allExercise = mutableListOf<Int>()
        val lastItem = newExercise.exercises.lastOrNull()
        if (lastItem != null) {
            for (item in newExercise.exercises) {
                DatabaseService.updateRow(
                    "exercise", item.toMap(), context,
                    { response ->
                        allExercise.add(response.getInt("id"))
                        if (item == lastItem) {
                            saveFullExercise(newExercise, allExercise, context)
                        }
                        Log.d("API_SINGLE_EXERCISES_CREATED", response.toString())
                    },
                    { error -> Log.e("API_ERROR", error.toString()) }
                )
            }
        } else {
            saveFullExercise(newExercise, allExercise, context)
        }
    }

    fun saveFullExercise(newExercise: ExerciseProgram, singleExerciseIds: List<Int>, context: Context) {
        val fullMap = newExercise.toMap().toMutableMap().apply {
            this["attributes"] = Json.encodeToString(VariableModel.newExercise.value.attributes)
            this["exercise"] = Json.encodeToString(singleExerciseIds)
        }
        DatabaseService.updateRow("exercise_program", fullMap, context,
            { response ->
                VariableModel.newExercise.value.id = response.getInt("id")
                VariableModel.allExercises.add(VariableModel.newExercise.value)
                Log.d("API_GOAL_UPDATED", response.toString())
            },
            { error -> Log.e("API_ERROR", error.toString())}
        )
    }

    fun saveNewRecipe (newRecipe: Map<String, Any?>, context: Context) {
        newRecipe.toMutableMap().apply {
            this["attributes"] = Json.encodeToString(VariableModel.newRecipe.value.attributes)
            this["steps"] = Json.encodeToString(VariableModel.newRecipe.value.steps)
            this["ingredients"] = Json.encodeToString(this["ingredients"])
        }
        DatabaseService.updateRow("food_recipe", newRecipe, context,
            { response ->
                VariableModel.newRecipe.value.id = response.getInt("id")
                VariableModel.allFoods.add(VariableModel.newRecipe.value)
                Log.d("API_RECIPE_CREATED", response.toString())
            },
            { error -> Log.e("API_ERROR", error.toString())}
        )
    }

//    TODO(): possibly delete later
    fun loadCategory (context: Context){
        DatabaseService.fetchTable(
            "category", context,
            { categories ->
                saveExistingCategories(categories)
                Log.d("API_SUCCESS_CATEGORIES", categories.toString())
            },
            { error -> Log.e("API_ERROR", error.toString()) }
        )
    }

    private fun saveExistingCategories(response: JSONArray) {
        val newItems = JSONObject()
        for (i in 0 until response.length()) {
            val category = response.getJSONObject(i)
            val id = category.getString("id")
            val name = category.getString("name")
            newItems.put(id, name)
        }
        VariableModel.existingCategories.value = newItems

    }

    fun saveCategory(context: Context) {
        val allCats = VariableModel.existingCategories.value.keys().asSequence().toList()

        val existingId = allCats.find { key ->
            VariableModel.existingCategories.value.getString(key) == VariableModel.categoryValue.value
        }

        if (existingId == null) {
            DatabaseService.updateRow(
                "category", mapOf("name" to VariableModel.categoryValue.value), context,
                { savedCategory ->
                    VariableModel.goalCategory.value.put("id", savedCategory.get("id"))
                    VariableModel.goalCategory.value.put("name", VariableModel.categoryValue.value)
                    Log.d("API_SUCCESS", savedCategory.toString())
                },
                { error -> Log.e("API_ERROR", error.toString()) }
            )
        } else {
            VariableModel.goalCategory.value.put("id", existingId.toInt())
            VariableModel.goalCategory.value.put("name", VariableModel.categoryValue.value)
        }
    }
}