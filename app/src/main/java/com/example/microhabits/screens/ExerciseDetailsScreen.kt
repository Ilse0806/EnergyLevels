package com.example.microhabits.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.microhabits.ExerciseDetails

@Composable
fun ExerciseDetailsScreen(navController: NavController, exerciseId: ExerciseDetails) {
    Text(text = exerciseId.id.toString())
}