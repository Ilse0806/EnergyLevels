package com.example.microhabits.models.classes

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.microhabits.ExerciseDetails
import com.example.microhabits.models.superclasses.UpperActivity

class ExerciseProgram (
    id: Int,
    name: String,
    description: String,
    time: Int,
    difficulty: Int,
    attributes: List<String>,
    var exercises: List<SingleExercise>,
    var icon: String,
    var recommended: Boolean
): UpperActivity(id, name, description, time, difficulty, attributes) {
    override fun toNavigationOption(): NavigationOption<*> {
        val baseOption = super.toNavigationOption()
        return NavigationOption(
            label = baseOption.label,
            destination = ExerciseDetails(this.id),
            image = this.icon
        )
    }

    fun iconFromString(): ImageVector? {
        return when (this.icon) {
            "DirectionsWalk" -> Icons.AutoMirrored.Filled.DirectionsWalk
            "FitnessCenter" -> Icons.Default.FitnessCenter
            else -> null
        }
    }
}