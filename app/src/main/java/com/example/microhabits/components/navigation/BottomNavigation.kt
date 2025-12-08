package com.example.microhabits.components.navigation

import android.os.Build
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.microhabits.BottomNavItem
import com.example.microhabits.Exercise
import com.example.microhabits.Food
import com.example.microhabits.Home
import com.example.microhabits.Profile
import com.example.microhabits.Progress
import com.example.microhabits.data.state.VariableModel
import com.example.microhabits.ui.theme.Typography

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