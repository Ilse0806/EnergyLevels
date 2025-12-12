package com.example.microhabits.services

import android.content.Context
import android.util.Log
import com.example.microhabits.api.DatabaseService
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.helpers.toExerciseProgram
import com.example.microhabits.helpers.toFoodRecipe
import com.example.microhabits.helpers.toSingleExercise
import com.example.microhabits.models.classes.SingleExercise

object FavoritesService {
    fun loadFavorites(context: Context, userId: Int) {
        DatabaseService.getRow(
            "user_favorite", mapOf("user_id" to userId, "fetch_one" to true), context,
            { favorites ->
                saveFoodFavorites(context, favorites["favorite_food"].toString().toList().mapNotNull { it.toString().toIntOrNull() })
                saveExerciseFavorites(context, favorites["favorite_exercise"].toString().toList().mapNotNull { it.toString().toIntOrNull() })

                Log.d("GET_FAVORITES_SUCCESSFUL", favorites.toString())
            },
            { error -> Log.e("GET_FAVORITES_ERROR", error.toString()) }
        )
    }

    fun saveFoodFavorites(context: Context, favorite: List<Int>) {
        VariableModel.favoriteDinner.clear()
        VariableModel.favoriteLunch.clear()
        VariableModel.favoriteBreakfast.clear()
        VariableModel.favoriteSnack.clear()
        DatabaseService.getRow(
            "food_recipe", mapOf("id" to favorite, "fetch_one" to false), context,
            { food ->
                val rows = food.getJSONArray("rows")
                for (i in 0 until rows.length()) {
                    val foodJson = rows.getJSONObject(i)
                    when (foodJson.getString("type")) {
                        "Dinner" -> VariableModel.favoriteDinner.add(foodJson.toFoodRecipe())
                        "Lunch" -> VariableModel.favoriteLunch.add(foodJson.toFoodRecipe())
                        "Breakfast" -> VariableModel.favoriteBreakfast.add(foodJson.toFoodRecipe())
                        "Snack" -> VariableModel.favoriteSnack.add(foodJson.toFoodRecipe())
                        else -> VariableModel.favoriteSnack.add(foodJson.toFoodRecipe())
                    }
                }
                Log.d("GET_FOOD_SUCCESSFUL", food.toString())
            },
            { error -> Log.e("GET_FOOD_ERROR", error.toString()) }
        )
    }

    fun saveExerciseFavorites(context: Context, favorite: List<Int>) {
        VariableModel.favoriteExercises.clear()
        DatabaseService.getRow(
            "exercise_program", mapOf("id" to favorite, "fetch_one" to false), context,
            { exercises ->
                val rows = exercises.getJSONArray("rows")
                for (i in 0 until rows.length()) {
                    val exerciseJson = rows.getJSONObject(i)
                    val exerciseIds = (exerciseJson["exercise"].toString().toList()).mapNotNull { it.toString().toIntOrNull() }

                    DatabaseService.getRow(
                        "exercise", mapOf("id" to exerciseIds, "fetch_one" to false), context,
                        { exercise ->
                            val rows = exercise.getJSONArray("rows")
                            val gottenList = mutableListOf<SingleExercise>()
                            for(i in 0 until rows.length()) {
                                val json = rows.getJSONObject(i)

                                gottenList.add(json.toSingleExercise())
                            }
                            val newExercise = exerciseJson.toExerciseProgram(gottenList)
                            VariableModel.favoriteExercises.add(newExercise)

                            Log.d("GET_SINGLE_EXERCISE_SUCCESSFUL", exercise.toString())
                        },
                        { error -> Log.e("GET_SINGLE_EXERCISE_ERROR", error.toString()) }
                    )
                }
                Log.d("GET_EXERCISE_SUCCESSFUL", exercises.toString())
            },
            { error -> Log.e("GET_EXERCISE_ERROR", error.toString()) }
        )
    }
}