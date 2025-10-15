package com.example.microhabits

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.microhabits.ui.theme.MicroHabitsTheme

class CreateGoal : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MicroHabitsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Styles(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}