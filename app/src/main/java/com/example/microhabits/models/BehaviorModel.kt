package com.example.microhabits.models

object BehaviorModel {
    fun calculateGoldenBehavior() {
        val newGoldenBehavior = VariableModel.selectedBehaviors.filter { item ->
            val impactValue = item.userBehavior.impactSliderValue
            val feasibilityValue = item.userBehavior.feasibilitySliderValue
            impactValue > 5 && feasibilityValue > 5
        }

        newGoldenBehavior.forEach { item ->
            if (!VariableModel.goldenBehaviors.contains(item)) {
                VariableModel.goldenBehaviors.add(item)
            }
        }
    }

    fun sortItems(allItems: List<UserBehaviorWithBehavior>): List<UserBehaviorWithBehavior> {
        val sortedItems = allItems.sortedWith(
            compareBy(
                { it.userBehavior.impactSliderValue },
                { it.userBehavior.feasibilitySliderValue }
            )
        )

        return sortedItems
    }

    fun saveChangesBehavior() {
        println(VariableModel.chosenBehaviors.value)
    }

}