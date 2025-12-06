package com.example.microhabits.helpers

import com.example.microhabits.models.classes.Ingredient

fun String.toIngredient(): Ingredient {
    val amountExtras = setOf(
        "gram", "grams", "g",
        "kilogram", "kilograms", "kg",
        "milliliter", "milliliters", "ml",
        "liter", "liters", "l",
        "tablespoon", "tablespoons", "tbsp",
        "teaspoon", "teaspoons", "tsp",
        "clove", "cloves",
        "pinch", "pinches"
    )

    val parts = this.trim().split(" ")
    var amount = 0.0
    var amountExtra = ""
    val nameParts = mutableListOf<String>()

    for (part in parts) {
        val number = part.toDoubleOrNull()
        if (number != null && amount == 0.0) {
            amount = number
        } else if (amountExtras.contains(part.lowercase()) && amountExtra.isBlank()) {
            amountExtra = part
        } else {
            nameParts.add(part)
        }
    }

    val name = nameParts.joinToString(" ")

    return Ingredient(
        name = name,
        Amount = amount,
        AmountExtra = amountExtra
    )
}