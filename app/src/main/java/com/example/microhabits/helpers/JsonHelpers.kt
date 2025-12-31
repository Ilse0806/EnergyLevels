package com.example.microhabits.helpers

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.microhabits.models.classes.Behavior
import com.example.microhabits.models.classes.CompletedGoal
import com.example.microhabits.models.classes.EnergyLevel
import com.example.microhabits.models.classes.ExerciseProgram
import com.example.microhabits.models.classes.FoodRecipe
import com.example.microhabits.models.classes.SingleExercise
import com.example.microhabits.models.enums.MeasuredInResult
import com.example.microhabits.models.enums.NotificationFrequency
import com.example.microhabits.models.classes.UserBehavior
import com.example.microhabits.models.classes.UserBehaviorWithBehavior
import com.example.microhabits.models.classes.UserGoal
import com.example.microhabits.models.enums.EnergyLevels
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun JSONObject.toUserBehavior(): UserBehavior {
    return UserBehavior(
        id = this.optInt("id"),
        goldenBehavior = this.optBoolean("golden_behavior"),
        oldBehavior = this.optBoolean("old_behavior"),
        progress = this.optInt("progress"),
        timeS = this.optLong("time_s"),
        impactSliderValue = this.optDouble("impactSliderValue", 1.0).toFloat(),
        feasibilitySliderValue = this.optDouble("impactSliderValue", 1.0).toFloat(),
        notification = this.optBoolean("notification"),
        completedToday = this.optBoolean("completed_today"),
        notificationFrequency = NotificationFrequency.fromInput(this.getString("notification_frequency")),
        notificationInterval = this.optInt("notification_interval"),
        notificationDay = try {
            DayOfWeek.valueOf(this.optString("notification_day", "MONDAY"))
        } catch (e: Exception) {
            DayOfWeek.MONDAY
        },
        notificationTimeOfDay = try {
            LocalTime.parse(this.optString("notification_time_of_day", "00:00"))
        } catch (e: Exception) {
            LocalTime.MIDNIGHT
        },
        startDate = try {
            LocalDate.parse(this.optString("start_date", LocalDate.now().toString()))
        } catch (e: Exception) {
            LocalDate.now()
        },
        behaviorId = this.optInt("behavior_id"),
        userId = this.optInt("user_id"),
        goalId = this.optInt("goal_id"),
        isAdded = this.optBoolean("isAdded"),
    )
}

fun JSONObject.toBehavior(completedToday: Boolean = false): Behavior {
    return Behavior(
        id = this.optInt("id"),
        name = this.optString("name"),
        description = this.optString("description").takeUnless { it == "null" } ?: "",
        measuredIn = MeasuredInResult.fromInput(this.optString("measured_in")),
        categoryId = this.optInt("category_id"),
        completedToday = completedToday
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun JSONObject.toUserBehaviorWithBehavior(): UserBehaviorWithBehavior {
    val id = this.optInt("id")
    val userBehavior = this.getJSONObject("user_behavior").toUserBehavior()
    val behavior = this.getJSONObject("behavior").toBehavior(userBehavior.completedToday)
    return UserBehaviorWithBehavior(id, userBehavior, behavior)
}

@RequiresApi(Build.VERSION_CODES.O)
fun JSONObject.toUserGoal(goalId: Int): UserGoal {
    return UserGoal(
        id = this.optInt("id"),
        goalId = goalId,
        name = this.optString("name"),
        description = this.optString("description").takeUnless { it == "null" } ?: "",
//        Long long line, to turn string in certain date format to LocalDateTime :)
        deadline = if (this.optString("deadline") != "null") {
            ZonedDateTime.parse(this.optString("deadline"), DateTimeFormatter.RFC_1123_DATE_TIME)
                .toLocalDateTime()
        } else {
            null
        },
        category = this.optString("category")
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun JSONObject.toCompletedGoal(): CompletedGoal {
    val time = this.optString("completion_time").split(":")

    return CompletedGoal(
        goalId = this.optInt("user_goal_id"),
        dateCompleted = LocalDateTime.of(
            this.optInt("year"),
            this.optInt("month"),
            this.optInt("day"),
            time[0].toInt(),
            time[1].toInt(),
        ),
    )
}

fun JSONObject.toExerciseProgram(singleExercises: List<SingleExercise>): ExerciseProgram {
    val attributesString = this.optString("attributes")
    val attributes = try {
        val asJson = JSONArray(attributesString)
        List(asJson.length()) { index ->
            asJson.getString(index)
        }
    } catch (e: JSONException) {
        Log.e("JSON_ERROR", e.toString())
        emptyList()
    }
    return ExerciseProgram(
        id = this.optInt("id"),
        name = this.optString("name"),
        description = this.optString("description"),
        time = this.optInt("time"),
        difficulty = this.optInt("difficulty"),
        attributes = attributes,
        exercises = singleExercises,
        icon = this.optString("icon"),
        recommended = this.optBoolean("recommended")
    )
}

fun JSONObject.toFoodRecipe(): FoodRecipe {
    val attributesString = this.optString("attributes")
    val attributes = try {
        val asJson = JSONArray(attributesString)
        List(asJson.length()) { index ->
            asJson.getString(index)
        }
    } catch (e: JSONException) {
        Log.e("JSON_ERROR", e.toString())
        emptyList()
    }

    val ingredientsString = this.optString("ingredients")
    val ingredients = try {
        val asJson = JSONArray(ingredientsString)
        List(asJson.length()) { index ->
            asJson.getString(index)
        }
    } catch (e: JSONException) {
        Log.e("JSON_ERROR", e.toString())
        emptyList()
    }

    val stepsString = this.optString("steps")
    val steps = try {
        val asJson = JSONArray(stepsString)
        List(asJson.length()) { index ->
            asJson.getString(index)
        }
    } catch (e: JSONException) {
        Log.e("JSON_ERROR", e.toString())
        emptyList()
    }

    return FoodRecipe(
        id = this.optInt("id"),
        name = this.optString("name"),
        description = this.optString("description"),
        time = this.optInt("time"),
        difficulty = this.optInt("difficulty"),
        attributes = attributes,
        type = this.optString("type"),
        image = this.optString("image"),
        ingredients = ingredients.map { it.toIngredient() },
        steps = steps,
        recommended = this.optBoolean("recommended")
    )
}

fun JSONObject.toSingleExercise(): SingleExercise {
    return SingleExercise(
        id = this.optInt("id"),
        name = this.optString("name"),
        description = this.optString("description"),
        time = this.optInt("time"),
        image = this.optString("image"),
        video = this.optString("video")
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun JSONObject.toEnergyLevel(): EnergyLevel {
    return EnergyLevel(
        date = LocalDate.parse(this.optString("date"), DateTimeFormatter.RFC_1123_DATE_TIME),
        level = EnergyLevels.fromInput(this.optString("energy_level")),
        reason = this.optString("reason")
    )
}