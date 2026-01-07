package com.example.microhabits.models.classes

class Ingredient (
    var name: String,
    var amount: Double,
    var amountExtra: String,
) {
    fun calculateAmount (portions: Int, oldPortions: Int = 4): Double {
        return this.amount / oldPortions * portions
    }

    fun toDisplayString(): String {
        return "$amount $amountExtra $name"
    }
}