package com.example.microhabits.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import org.json.JSONObject

object VariableModel {
    var navBarHeight = mutableIntStateOf(0)

    var user by mutableStateOf<JSONObject?>(null)
    var userName by mutableStateOf("")
    var userId by mutableIntStateOf(0)
    val allBehaviors = mutableStateListOf<Map<String, Any?>>()
    val todayBehaviors = mutableStateListOf<Behavior>()
    val userGoals = mutableStateListOf<Map<String, Any?>>()

    val connectedBehaviors = mutableStateListOf<Behavior>() // behavior
    val detailsBehaviors = mutableStateListOf<UserBehavior>() // probably user-behavior
    val combinedBehaviors = mutableStateListOf<UserBehaviorWithBehavior>() // both

    var validGoal = mutableStateOf(false)
    var categoryValue = mutableStateOf("")
    var goal = mutableStateOf("")
    var goalCategory = mutableStateOf(JSONObject())
    var existingCategories = mutableStateOf(JSONObject())

    var selectedBehaviors = mutableStateListOf<UserBehaviorWithBehavior>() // behavior
    var personalizedBehaviors = mutableStateListOf<UserBehaviorWithBehavior>() // behavior
    var goldenBehaviors = mutableStateListOf<UserBehaviorWithBehavior>() // behavior

    var chosenBehaviors = mutableStateOf(listOf<UserBehaviorWithBehavior>()) // holds multiple
}