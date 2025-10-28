package com.example.microhabits

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.microhabits.models.VariableModel
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import kotlinx.serialization.Serializable


// All objects for navigation + global variables are initialized here:
@Serializable
object Home
@Serializable
data class Profile(val user: String)
@Serializable
object Progress
@Serializable
object CreateGoal
@Serializable
data class DisplayGoal(val goal: String)
@Serializable
data class DisplayBehavior(val behavior: String)
@Serializable
data class CreateBehavior(val goal: String)

data class BottomNavItem(
    val destination: () -> Any,
    val routeName: String,
    val title: String,
    val icon: ImageVector
)

var navBarHeight = mutableIntStateOf(0)

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MicroHabitsTheme(dynamicColor = false){
                val navController = rememberNavController()

                NavHost(navController, startDestination = Home) {
                    composable<Home> { HomeScreen(navController) }
                    composable<Progress> { ProgressScreen(navController) }
                    composable<CreateGoal> { CreateGoalScreen(navController) }
                    composable<Profile> { backStackEntry ->
                        val user: Profile = backStackEntry.toRoute()
                        ProfileScreen(navController, user)
                    }
                    composable<DisplayGoal> { backStackEntry ->
                        val goal: DisplayGoal = backStackEntry.toRoute()
                        DisplayGoalScreen(navController, goal)
                    }
                    composable<DisplayBehavior> { backStackEntry ->
                        val behavior: DisplayBehavior = backStackEntry.toRoute()
                        DisplayBehaviorScreen(navController, behavior)
                    }
                    composable<CreateBehavior> { backStackEntry ->
                        val newGoal: CreateBehavior = backStackEntry.toRoute()
                        CreateBehaviorScreen(navController, newGoal)
                    }
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(navController: NavController) {
    val bottomNavItems = listOf(
        BottomNavItem({ Home }, "com.example.microhabits.Home", "Home", Icons.Default.Home),
        BottomNavItem({ Progress }, "com.example.microhabits.Progress", "Progress", Icons.Filled.DateRange),
        BottomNavItem({ CreateGoal }, "com.example.microhabits.CreateGoal", "Create goal", Icons.Default.AddCircle),
        BottomNavItem({ Profile(VariableModel.user.toString()) }, "com.example.microhabits.Profile", "Profile", Icons.Default.Person),
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .onGloballyPositioned{ coordinates ->
                navBarHeight.intValue = coordinates.size.height
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

@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MicroHabitsTheme(dynamicColor = false) {
        Scaffold(
            bottomBar = {
                if (!WindowInsets.isImeVisible) {
                    Navigation(rememberNavController())
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ) {
                Text("hi")
            }
            GoalsDisplay(rememberNavController())
        }
    }
}