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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavController) {
    val bottomNavItems = listOf(
        BottomNavItem({ Exercise }, "com.example.microhabits.Exercise", "Exercise", Icons.Default.SportsGymnastics),
        BottomNavItem({ Food }, "com.example.microhabits.Food", "Food", Icons.Default.Dining),
        BottomNavItem({ Home }, "com.example.microhabits.Home", "Home", Icons.Default.Home),
        BottomNavItem({ Progress }, "com.example.microhabits.Progress", "Progress", Icons.Filled.DateRange),
        BottomNavItem({ Profile(VariableModel.user.toString()) }, "com.example.microhabits.Profile", "Profile", Icons.Default.Person),
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .onGloballyPositioned{ coordinates ->
                VariableModel.navBarHeight.intValue = coordinates.size.height
            }
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.routeName

            NavigationBarItem(
                icon = {
                    if (selected)
                        Icon(item.icon, contentDescription = item.title, modifier = Modifier.size(26.dp))
                    else
                        Icon(item.icon, contentDescription = item.title) },
                label = {
                    if (selected)
                        Text(item.title, fontWeight = FontWeight.Bold, style = Typography.labelMedium)
                    else
                        Text(item.title, style = Typography.labelMedium) },
                selected = selected,
                colors = NavigationBarItemColors(
                    selectedIconColor = MaterialTheme.colorScheme.tertiary,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.tertiary,
                    unselectedTextColor = MaterialTheme.colorScheme.secondary,
                    selectedIndicatorColor = Color.Transparent,
                    disabledIconColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                    disabledTextColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                ),
                onClick = {
                    navController.navigate(item.destination()) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }

}
