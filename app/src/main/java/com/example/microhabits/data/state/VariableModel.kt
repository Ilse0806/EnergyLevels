package com.example.microhabits.data.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.microhabits.models.classes.Behavior
import com.example.microhabits.models.classes.ExerciseProgram
import com.example.microhabits.models.classes.FoodRecipe
import com.example.microhabits.models.classes.UserBehavior
import com.example.microhabits.models.classes.UserBehaviorWithBehavior
import org.json.JSONObject

object VariableModel {
    var navBarHeight = mutableIntStateOf(0)

//    User data:
    var user by mutableStateOf<JSONObject?>(null)
    var userName by mutableStateOf("")
    var userId by mutableIntStateOf(0)
    val userGoals = mutableStateListOf<Map<String, Any?>>()

//    Activity data:
    val favoriteFoods = mutableStateListOf<FoodRecipe>()
    val favoriteExercises = mutableStateListOf<ExerciseProgram>()

//    Goal / category data:
    var validGoal = mutableStateOf(false)
    var categoryValue = mutableStateOf("")
    var goal = mutableStateOf("")
    var goalCategory = mutableStateOf(JSONObject())
    var existingCategories = mutableStateOf(JSONObject())

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