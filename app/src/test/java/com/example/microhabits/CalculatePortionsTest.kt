package com.example.microhabits

import com.example.microhabits.models.classes.Ingredient
import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatePortionsTest {
    val testIngedrient1 = Ingredient("Test ingredient 1", 400.0, "grams")
    val testIngedrient2 = Ingredient("Test ingredient 1", 2.0, "tbsp")
    val testIngedrient3 = Ingredient("Test ingredient 1", 1.0, "")
    val testIngedrient4 = Ingredient("Test ingredient 1", 3.0, "")

    @Test
    fun calculation_isCorrect() {
        assertEquals(600.0, testIngedrient1.calculateAmount(6), 0.01)
        assertEquals(1.0, testIngedrient2.calculateAmount(2), 0.01)
        assertEquals(0.25, testIngedrient3.calculateAmount(1), 0.001)
        assertEquals(6.0, testIngedrient4.calculateAmount(8), 0.01)
        assertEquals(300.0, testIngedrient1.calculateAmount(3), 0.01)
        assertEquals(2.5, testIngedrient2.calculateAmount(5), 0.01)
        assertEquals(0.75, testIngedrient3.calculateAmount(3), 0.001)
        assertEquals(5.25, testIngedrient4.calculateAmount(7), 0.001)
    }
}