package com.example.microhabits

import com.example.microhabits.helpers.toCompletedGoal
import com.example.microhabits.helpers.toExerciseProgram
import com.example.microhabits.helpers.toFoodRecipe
import com.example.microhabits.helpers.toIngredient
import com.example.microhabits.helpers.toSingleExercise
import com.example.microhabits.helpers.toUserGoal
import org.json.JSONObject
import org.junit.Test
import java.time.LocalDateTime
import org.junit.Assert.assertEquals

class JsonHelperTest {
    val testUserGoal1 = JSONObject("""{"id":"1","name":"Get healthier","description":"Get healthy or something","deadline":"Fri, 12 Dec 2025 11:51:31 GMT","category":"Exercise"}""")
    val testUserGoal2 = JSONObject("""{"id":"1","name":"Eat more fruit","description":"","deadline":"Fri, 12 Dec 2025 11:51:31 GMT","category":"Food"}""")
    val testCompletedGoal1 = JSONObject("""{"id":5,"user_goal_id":5,"year":2025,"month":8,"day":30,"completion_time":"21:08:52"}""")
    val testCompletedGoal2 = JSONObject("""{"id":684,"user_goal_id":57464,"year":2006,"month":5,"day":21,"completion_time":"21:08:52"}""")
    val testExerciseProgram1 = JSONObject("""{"id":684,"name":"Run 4 km","description":"Run a little bit","time":30,"difficulty":8,"attributes":["cardio"],"exercise":[1],"icon":"DirectionsWalk","recommended":1}""")
    val testExerciseProgram2 = JSONObject("""{"id":684,"name":"Abs exercise","description":"Do a couple of ab exercises","time":45,"difficulty":5,"attributes":[],"exercise":[4,8,10],"icon":"FitnessCenter","recommended":0}""")
    val testFoodRecipe1 = JSONObject("""{"id":684,"name":"Chicken fajitas","time":45,"difficulty":3,"attributes":["Chicken"],"type":"Dinner","image":"","ingredients":["1 red bell pepper"],"steps":["cook","boil","eat"],"recommended":0}""")
    val testFoodRecipe2 = JSONObject("""{"id":684,"name":"Noodles","time":30,"difficulty":2,"attributes":["Asian","Vegetarian"],"type":"Dinner","image":"","ingredients":["1 red bell pepper"],"steps":["cook","boil","eat"],"recommended":1}""")
    val testExercise1 = JSONObject("""{"id":684,"name":"Abs 1","description":"Plank","image":"","video":"","time":10}""")
    val testExercise2 = JSONObject("""{"id":684,"name":"Abs 2","description":"Side plank","image":"","video":"","time":15}""")

    @Test
    fun toUserGoal() {
        val result1 = testUserGoal1.toUserGoal(5)
        assertEquals("Get healthier", result1.name)
        assertEquals("Get healthy or something", result1.description)
        assertEquals(LocalDateTime.of(2025,12,12,11,51,31), result1.deadline)
        assertEquals("Exercise", result1.category)

        val result2 = testUserGoal2.toUserGoal(5)
        assertEquals("Eat more fruit", result2.name)
        assertEquals("", result2.description)
        assertEquals(LocalDateTime.of(2025,12,12,11,51,31), result2.deadline)
        assertEquals("Food", result2.category)
    }

    @Test
    fun toCompletedGoal() {
        val result1 = testCompletedGoal1.toCompletedGoal()
        assertEquals(5, result1.goalId)
        assertEquals(LocalDateTime.of(2025,8,30,21,8), result1.dateCompleted)

        val result2 = testCompletedGoal2.toCompletedGoal()
        assertEquals(57464, result2.goalId)
        assertEquals(LocalDateTime.of(2006,5,21,21,8), result2.dateCompleted)
    }

    @Test
    fun toExerciseProgram() {
        val result1 = testExerciseProgram1.toExerciseProgram(listOf(testExercise1.toSingleExercise()))
        assertEquals("Run 4 km", result1.name)
        assertEquals("Run a little bit", result1.description)
        assertEquals("DirectionsWalk", result1.icon)
        assertEquals(8, result1.difficulty)
        assertEquals(30, result1.time)
        assertEquals(listOf("cardio"), result1.attributes)

        val result2 = testExerciseProgram2.toExerciseProgram(listOf(testExercise2.toSingleExercise()))
        assertEquals("Abs exercise", result2.name)
        assertEquals("Do a couple of ab exercises", result2.description)
        assertEquals("FitnessCenter", result2.icon)
        assertEquals(5, result2.difficulty)
        assertEquals(45, result2.time)
        assertEquals(listOf<String>(), result2.attributes)
        assertEquals(false, result2.recommended)
    }

    @Test
    fun toFoodRecipe() {
        val result1 = testFoodRecipe1.toFoodRecipe()
        assertEquals("Chicken fajitas", result1.name)
        assertEquals("", result1.description)
        assertEquals(45, result1.time)
        assertEquals(3, result1.difficulty)
        assertEquals("Dinner", result1.type)
        assertEquals(false, result1.recommended)
        assertEquals(listOf("cook", "boil", "eat"), result1.steps)
        assertEquals(listOf("1 red bell pepper".toIngredient().name), result1.ingredients.map { it.name })
        assertEquals("", result1.image)
        assertEquals(listOf("Chicken"), result1.attributes)

        val result2 = testFoodRecipe2.toFoodRecipe()
        assertEquals("Noodles", result2.name)
        assertEquals("", result2.description)
        assertEquals(30, result2.time)
        assertEquals(2, result2.difficulty)
        assertEquals("Dinner", result2.type)
        assertEquals(false, result2.recommended)
        assertEquals(listOf("cook", "boil", "eat"), result2.steps)
        assertEquals(listOf("1 red bell pepper".toIngredient().name), result2.ingredients.map { it.name })
        assertEquals("", result2.image)
        assertEquals(listOf("Asian","Vegetarian"), result2.attributes)
    }

    @Test
    fun toSingleExercise() {
        val result1 = testExercise1.toSingleExercise()
        assertEquals("Abs 1", result1.name)
        assertEquals("Plank", result1.description)
        assertEquals(10, result1.time)
        assertEquals("", result1.image)
        assertEquals("", result1.video)

        val result2 = testExercise2.toSingleExercise()
        assertEquals("Abs 2", result2.name)
        assertEquals("Side plank", result2.description)
        assertEquals(15, result2.time)
        assertEquals("", result2.image)
        assertEquals("", result2.video)
    }
}