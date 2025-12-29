package com.example.microhabits

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.microhabits.api.DatabaseService
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.concurrent.CountDownLatch

class DatabaseTest {
    val newUser = mapOf(
        "first_name" to "Test",
        "last_name" to "Person",
        "gender" to "Other",
        "birth_date" to LocalDate.of(2025, 12, 19)
    )

    val updatedGoal = mapOf(
        "id" to 4,
        "name" to "Testing goal",
        "description" to "I am testing loads of things",
        "deadline" to LocalDateTime.of(2025, 12, 19, 12, 0),
        "category" to "Food"
    )

    @Test
    fun databaseCreateUser_isCorrect() {
        val latch = CountDownLatch(1)
        var result: String? = null
        val context: Context = ApplicationProvider.getApplicationContext()

        DatabaseService.updateRow(
            "user", newUser, context,
            { success ->
                result = success.optString("message")
                latch.countDown()
            },
            { error ->
                result = error.toString()
                latch.countDown()
            }
        )
        latch.await()
        assertEquals("Row created", result)
    }

    @Test
    fun databaseUpdateGoal_isCorrect() {
        val latch = CountDownLatch(1)
        var result: String? = null
        val context: Context = ApplicationProvider.getApplicationContext()

        DatabaseService.updateRow(
            "goal", updatedGoal, context,
            { success ->
                result = success.optString("message")
                latch.countDown()
            },
            { error ->
                result = error.toString()
                latch.countDown()
            }
        )
        latch.await()
        assertEquals("Row updated", result)
    }

    @Test
    fun databaseDeleteGoal_isCorrect() {
        val latch = CountDownLatch(1)
        var result: String? = null
        val context: Context = ApplicationProvider.getApplicationContext()

        DatabaseService.deleteRow(
            "goal", mapOf("id" to 5), context,
            { success ->
                println(success)
                println(success.optString("message"))
                result = success.optString("message")
                latch.countDown()
            },
            { error ->
                result = error.toString()
                latch.countDown()
            }
        )

        latch.await()
        assertEquals("Row deleted", result)
    }

    @Test
    fun databaseGetGoal_isCorrect() {
        val latch = CountDownLatch(1)
        var result: String? = null
        val context: Context = ApplicationProvider.getApplicationContext()

        DatabaseService.getRow(
            "goal", mapOf("id" to 7, "fetch_one" to "true"), context,
            { success ->
                result = success.optString("error")
                latch.countDown()
            },
            { error ->
                if (error.networkResponse != null) {
                    val body = String(
                        error.networkResponse.data,
                        Charsets.UTF_8
                    )
                    val json = JSONObject(body)
                    result = json.optString("error")
                } else {
                    result = error.toString()
                }
                latch.countDown()
            }
        )
        latch.await()
        assertEquals("Row not found", result)
    }

    @Test
    fun databaseGetUser_isCorrect() {
        val latch = CountDownLatch(1)
        var result: String? = null
        val context: Context = ApplicationProvider.getApplicationContext()

        DatabaseService.getRow(
            "user", mapOf("first_name" to "Bob", "fetch_one" to true), context,
            { success ->
                result = success.optString("last_name")
                latch.countDown()
            },
            { error ->
                result = error.toString()
                latch.countDown()
            }
        )
        latch.await()
        assertEquals("the Builder", result)
    }
}