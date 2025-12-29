package com.example.microhabits.models.classes

class Ingredient (
    var name: String,
    var amount: Double,
    var amountExtra: String,
) {
    fun CalculateAmount (portions: Int, oldPortions: Int = 4): Double {
//        TODO(): add logic so the default amount is translated to the given portion size.
//        default portions are 4, so for 4 people
        return this.amount / oldPortions * portions

//        return Ingredient(this.name, newAmount, this.amountExtra)
    }
}