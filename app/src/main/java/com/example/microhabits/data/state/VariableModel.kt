package com.example.microhabits.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.microhabits.models.deleteLater.Behavior
import com.example.microhabits.models.classes.CompletedGoal
import com.example.microhabits.models.classes.EnergyLevel
import com.example.microhabits.models.classes.ExerciseProgram
import com.example.microhabits.models.classes.FoodRecipe
import com.example.microhabits.models.classes.Goal
import com.example.microhabits.models.classes.Ingredient
import com.example.microhabits.models.classes.SingleExercise
import com.example.microhabits.models.deleteLater.UserBehavior
import com.example.microhabits.models.deleteLater.UserBehaviorWithBehavior
import com.example.microhabits.models.classes.UserGoal
import org.json.JSONObject

object VariableModel {
    var navBarHeight = mutableIntStateOf(0)

//    User data:
    var user by mutableStateOf<JSONObject?>(null)
    var userName by mutableStateOf("")
    var userId by mutableIntStateOf(0)
    val userGoals = mutableStateListOf<UserGoal>()
    val completedGoals = mutableStateListOf<CompletedGoal>()

//    Food data:
    val favoriteDinner = mutableStateListOf<FoodRecipe>()
    val favoriteLunch = mutableStateListOf<FoodRecipe>()
    val favoriteBreakfast = mutableStateListOf<FoodRecipe>()
    val favoriteSnack = mutableStateListOf<FoodRecipe>()
    val recommendedDinner = mutableStateListOf<FoodRecipe>()
    val recommendedLunch = mutableStateListOf<FoodRecipe>()
    val recommendedBreakfast = mutableStateListOf<FoodRecipe>()
    val recommendedSnack = mutableStateListOf<FoodRecipe>()
    val allFoods = mutableStateListOf<FoodRecipe>()

//    Exercise data:
    val favoriteExercises = mutableStateListOf<ExerciseProgram>()
    val allExercises = mutableStateListOf<ExerciseProgram>()
    val recommendedExercise = mutableStateListOf<ExerciseProgram>()

//    new activity data:
    val allIngredients = mutableStateListOf(Ingredient("Test", 3.0, "test"))
    val newIngredients = mutableStateListOf<Ingredient>()
    val newSingleExercises = mutableStateListOf<SingleExercise>()

    val newRecipe = mutableStateOf(FoodRecipe(
        id = null,
        name = "",
        description = "",
        time = 0,
        difficulty = 1,
        attributes = listOf(),
        type = "Dinner",
        image = "https://",
        ingredients = listOf(),
        steps = listOf(),
        recommended = false,
    ))

    val newExercise = mutableStateOf(ExerciseProgram(
        id = null,
        name = "",
        description = "",
        time = 0,
        difficulty = 1,
        attributes = listOf(),
        exercises = listOf(),
        icon = "DirectionsWalk",
        recommended = false
    ))
    val existingAttributes = mutableStateListOf<String>("Cardio", "Weights", "Strength", "Endurance", "Stretches")

    //    Goal / category data:
    var validGoal = mutableStateOf(false)
    var categoryValue = mutableStateOf("")
    var goal = mutableStateOf<Goal?>(null)
    var goalCategory = mutableStateOf(JSONObject())
    var existingCategories = mutableStateOf(JSONObject())

    var todayEnergy = mutableStateOf<EnergyLevel?>(null)

//    TODO(): Might remove
    val allBehaviors = mutableStateListOf<Map<String, Any?>>()
    val todayBehaviors = mutableStateListOf<Behavior>()

    val connectedBehaviors = mutableStateListOf<Behavior>()
    val detailsBehaviors = mutableStateListOf<UserBehavior>()
    val combinedBehaviors = mutableStateListOf<UserBehaviorWithBehavior>()

    var selectedBehaviors = mutableStateListOf<UserBehaviorWithBehavior>()
    var personalizedBehaviors = mutableStateListOf<UserBehaviorWithBehavior>()
    var goldenBehaviors = mutableStateListOf<UserBehaviorWithBehavior>()

    var chosenBehaviors = mutableStateOf(listOf<UserBehaviorWithBehavior>())

}