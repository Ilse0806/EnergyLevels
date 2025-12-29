package com.example.microhabits

import com.example.microhabits.helpers.toIngredient
import org.junit.Assert.assertEquals
import org.junit.Test

class StringHelperTest {
    val testString1 = "1 red bell pepper"
    val testString2 = "2 carrots"
    val testString3 = "3 cloves garlic"
    val testString4 = "26043"
    val testString5 = "onion"
    val testString6 = "grams"

    @Test
    fun normalIngredientCase_isCorrect() {
        val result1 = testString1.toIngredient()
        assertEquals("red bell pepper", result1.name)
        assertEquals(1.0, result1.amount, 0.01)
        assertEquals("", result1.amountExtra)

        val result2 = testString2.toIngredient()
        assertEquals("carrots", result2.name)
        assertEquals(2.0, result2.amount, 0.01)
        assertEquals("", result2.amountExtra)

        val result3 = testString3.toIngredient()
        assertEquals("garlic", result3.name)
        assertEquals(3.0, result3.amount, 0.01)
        assertEquals("cloves", result3.amountExtra)
    }

    @Test
    fun abnormalIngredientCase_IsCorrect() {
        val result1 = testString4.toIngredient()
        assertEquals("", result1.name)
        assertEquals(26043.0, result1.amount, 0.01)
        assertEquals("", result1.amountExtra)

        val result2 = testString5.toIngredient()
        assertEquals("onion", result2.name)
        assertEquals(0.0, result2.amount, 0.01)
        assertEquals("", result2.amountExtra)

        val result3 = testString6.toIngredient()
        assertEquals("", result3.name)
        assertEquals(0.0, result3.amount, 0.01)
        assertEquals("grams", result3.amountExtra)
    }
}