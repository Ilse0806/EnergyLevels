package com.example.microhabits.models.classes

import com.example.microhabits.ExerciseDetails
import com.example.microhabits.models.superclasses.UpperActivity

class FoodRecipe(
    id: Int?,
    name: String,
    description: String,
    time: Int,
    difficulty: Int,
    attributes: List<String>,
    var type: String,
    var image: String?,
    var ingredients: List<Ingredient>,
    var steps: List<String>,
    var recommended: Boolean
): UpperActivity(id, name, description, time, difficulty, attributes) {
    override fun toNavigationOption(): NavigationOption<*> {
        val baseOption = super.toNavigationOption()
        return NavigationOption(
            label = baseOption.label,
            destination = ExerciseDetails(this.id),
            image = this.image
        )
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "description" to description,
            "time" to time,
            "difficulty" to difficulty,
            "attributes" to attributes,
            "type" to type,
            "image" to image,
            "ingredients" to ingredients.map { it.toDisplayString() },
            "steps" to steps,
            "recommended" to recommended
        )
    }
}