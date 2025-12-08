package com.example.microhabits

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Dining
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.screens.CreateBehaviorScreen
import com.example.microhabits.screens.DisplayBehaviorScreen
import com.example.microhabits.screens.DisplayGoalScreen
import com.example.microhabits.screens.ExerciseDetailsScreen
import com.example.microhabits.screens.ExerciseScreen
import com.example.microhabits.screens.FocusMapScreen
import com.example.microhabits.screens.FoodDetailsScreen
import com.example.microhabits.screens.FoodScreen
import com.example.microhabits.screens.HomeScreen
import com.example.microhabits.screens.ProfileScreen
import com.example.microhabits.screens.ProgressScreen
import com.example.microhabits.screens.SelectBehaviorScreen
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
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
data class ExerciseDetails(val id: Int)
@Serializable
data class FoodDetails(val id: Int)

@Serializable
object CreateGoal
@Serializable
data class DisplayGoal(val goal: String)
@Serializable
data class DisplayBehavior(val behavior: String)
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

//                    composable<CreateGoal> { CreateGoalScreen(navController) }
                    composable<DisplayGoal> { backStackEntry ->
                        val goal: DisplayGoal = backStackEntry.toRoute()
                        DisplayGoalScreen(navController, goal)
                    }
                    composable<DisplayBehavior> { backStackEntry ->
                        val behavior: DisplayBehavior = backStackEntry.toRoute()
                        DisplayBehaviorScreen(navController, behavior)
                    }
                    composable<CreateBehavior> { CreateBehaviorScreen(navController) }
                    composable<FocusMap> { FocusMapScreen(navController) }
                    composable<SelectBehavior> { SelectBehaviorScreen(navController) }
                }
            }

        }
    }
}

