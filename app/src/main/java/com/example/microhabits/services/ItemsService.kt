package com.example.microhabits.services

import android.content.Context
import android.util.Log
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.helpers.toExerciseProgram
import com.example.microhabits.helpers.toFoodRecipe
import com.example.microhabits.helpers.toSingleExercise
import com.example.microhabits.models.classes.ExerciseProgram
import com.example.microhabits.models.classes.FoodRecipe
import com.example.microhabits.models.classes.SingleExercise

object ItemsService {
    fun loadItems(context: Context, userId: Int) {
        DatabaseService.getRow(
            "user_favorite", mapOf("user_id" to userId, "fetch_one" to true), context,
            { favorites ->
                DatabaseService.fetchTable(
                    "food_recipe", context,
                    { table ->
                        for (i in 0 until table.length()) {
                            val item = table.getJSONObject(i)
                            val food = item.toFoodRecipe()
                            setFoodItem(food, favorites["favorite_food"].toString().toList().mapNotNull { it.toString().toIntOrNull() })
                        }
                        Log.d("GET_ITEMS_SUCCESSFUL", table.toString())
                    },
                    { error -> Log.e("GET_ITEMS_ERROR", error.toString()) }
                )

                DatabaseService.fetchTable(
                    "exercise_program", context,
                    { table ->
                        for (i in 0 until table.length()) {
                            val item = table.getJSONObject(i)
                            val exerciseIds = (item["exercise"].toString().toList()).mapNotNull { it.toString().toIntOrNull() }

                            DatabaseService.getRow(
                                "exercise", mapOf("id" to exerciseIds, "fetch_one" to false), context,
                                { exercise ->
                                    val rows = exercise.getJSONArray("rows")
                                    val gottenList = mutableListOf<SingleExercise>()
                                    for(i in 0 until rows.length()) {
                                        val json = rows.getJSONObject(i)

                                        gottenList.add(json.toSingleExercise())
                                    }
                                    val newExercise = item.toExerciseProgram(gottenList)

                                    setExerciseItems(newExercise, favorites["favorite_exercise"].toString().toList().mapNotNull { it.toString().toIntOrNull() })

                                    Log.d("GET_SINGLE_EXERCISE_SUCCESSFUL", exercise.toString())
                                },
                                { error -> Log.e("GET_SINGLE_EXERCISE_ERROR", error.toString()) }
                            )
                        }
                        Log.d("GET_ITEMS_SUCCESSFUL", table.toString())
                    },
                    { error -> Log.e("GET_ITEMS_ERROR", error.toString()) }
                )

                Log.d("GET_FAVORITES_SUCCESSFUL", favorites.toString())
            },
            { error -> Log.e("GET_FAVORITES_ERROR", error.toString()) }
        )
    }

    private fun setFoodItem(item: FoodRecipe, userFavorites: List<Int>) {
        if (item.id in userFavorites) {
            when (item.type) {
                "Dinner" -> VariableModel.favoriteDinner.add(item)
                "Lunch" -> VariableModel.favoriteLunch.add(item)
                "Breakfast" -> VariableModel.favoriteBreakfast.add(item)
                "Snack" -> VariableModel.favoriteSnack.add(item)
                else -> VariableModel.favoriteSnack.add(item)
            }
        } else if (item.recommended) {
            when (item.type) {
                "Dinner" -> VariableModel.recommendedDinner.add(item)
                "Lunch" -> VariableModel.recommendedLunch.add(item)
                "Breakfast" -> VariableModel.recommendedBreakfast.add(item)
                "Snack" -> VariableModel.recommendedSnack.add(item)
                else -> VariableModel.recommendedSnack.add(item)
            }
        }
        VariableModel.allFoods.add(item)
    }

    private fun setExerciseItems(item: ExerciseProgram, userFavorites: List<Int>) {
        if (item.id in userFavorites) {
            VariableModel.favoriteExercises.add(item)
        } else if (item.recommended) {
            VariableModel.recommendedExercise.add(item)
        }
        VariableModel.allExercises.add(item)
    }
}