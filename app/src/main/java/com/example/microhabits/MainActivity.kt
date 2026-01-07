package com.example.microhabits

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.microhabits.screens.secondary.CreateGoalScreen
//import com.example.microhabits.screens.baseFunctionality.CreateBehaviorScreen
import com.example.microhabits.screens.baseFunctionality.DisplayGoalScreen
import com.example.microhabits.screens.details.ExerciseDetailsScreen
import com.example.microhabits.screens.main.ExerciseScreen
import com.example.microhabits.screens.baseFunctionality.FocusMapScreen
import com.example.microhabits.screens.details.FoodDetailsScreen
import com.example.microhabits.screens.main.FoodScreen
import com.example.microhabits.screens.main.HomeScreen
import com.example.microhabits.screens.main.ProfileScreen
import com.example.microhabits.screens.main.ProgressScreen
import com.example.microhabits.screens.secondary.CreateExerciseScreen
import com.example.microhabits.screens.secondary.CreateFoodScreen
import com.example.microhabits.screens.secondary.SetEnergyLevelScreen
//import com.example.microhabits.screens.baseFunctionality.SelectBehaviorScreen
import com.example.microhabits.ui.theme.MicroHabitsTheme
import kotlinx.serialization.Serializable


// All objects for navigation
@Serializable
object Home
@Serializable
data class Profile(val user: String)
@Serializable
object Progress
@Serializable
object Exercise
@Serializable
object Food

@Serializable
data class ExerciseDetails(val id: Int?)
@Serializable
data class FoodDetails(val id: Int?)

@Serializable
object CreateGoal
@Serializable
data class DisplayGoal(val goalId: Int)

@Serializable
object CreateExercise
@Serializable
object CreateFood

@Serializable
object SetEnergy

//@Serializable
//data class DisplayBehavior(val behavior: String)
@Serializable
object CreateBehavior
@Serializable
object FocusMap
@Serializable
object SelectBehavior

data class BottomNavItem(
    val destination: () -> Any,
    val routeName: String,
    val title: String,
    val icon: ImageVector
)

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MicroHabitsTheme(dynamicColor = false){
                val navController = rememberNavController()

                NavHost(navController, startDestination = Home) {
//                    Bottom navigation options:
                    composable<Exercise> { ExerciseScreen(navController) }
                    composable<Food> { FoodScreen(navController) }
                    composable<Home> { HomeScreen(navController) }
                    composable<Progress> { ProgressScreen(navController) }
                    composable<Profile> { backStackEntry ->
                        val user: Profile = backStackEntry.toRoute()
                        ProfileScreen(navController, user)
                    }
//                    Remaining screens:
                    composable<ExerciseDetails> { backstackEntry ->
                        val exerciseDetails: ExerciseDetails = backstackEntry.toRoute()
                        ExerciseDetailsScreen(navController, exerciseDetails)
                    }
                    composable<FoodDetails> { backstackEntry ->
                        val foodDetails: FoodDetails = backstackEntry.toRoute()
                        FoodDetailsScreen(navController, foodDetails)
                    }

                    composable<CreateGoal> { CreateGoalScreen(navController) }
                    composable<DisplayGoal> { backStackEntry ->
                        val goal: DisplayGoal = backStackEntry.toRoute()
                        DisplayGoalScreen(navController, goal)
                    }
                    composable<CreateExercise> { CreateExerciseScreen(navController) }
                    composable<CreateFood> { CreateFoodScreen(navController) }
                    composable<SetEnergy> { SetEnergyLevelScreen(navController)}

//                    composable<DisplayBehavior> { backStackEntry ->
//                        val behavior: DisplayBehavior = backStackEntry.toRoute()
//                        DisplayBehaviorScreen(navController, behavior)
//                    }
//                    composable<CreateBehavior> { CreateBehaviorScreen(navController) }
                    composable<FocusMap> { FocusMapScreen(navController) }
//                    composable<SelectBehavior> { SelectBehaviorScreen(navController) }
                }
            }

        }
    }
}

