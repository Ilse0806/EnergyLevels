package com.example.microhabits.archive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.borderStroke
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.microhabits.DisplayBehavior
import com.example.microhabits.models.deleteLater.UserBehaviorWithBehavior
import com.example.microhabits.models.deleteLater.UserGoal
import com.example.microhabits.ui.theme.ButtonColors
import com.example.microhabits.ui.theme.Color
import com.example.microhabits.ui.theme.Typography

@Composable
fun ArchivedSixColumnGrid(name: String, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(6),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        item(span = { GridItemSpan(4) }) {
            Box {
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
            }
        }
        item(span = { GridItemSpan(2) }) {
            Box {
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
                Text(
                    text = "Hello $name!",
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun ArchivedStyles(modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Title",
            modifier = modifier,
            style = Typography.titleLarge,
        )
        Text(
            text = "Heading 1",
            modifier = modifier,
            style = Typography.titleMedium,
        )
        Text(
            text = "Heading 2",
            modifier = modifier,
            style = Typography.titleSmall,
        )
        Text(
            text = "Paragraph",
            modifier = modifier,
            style = Typography.bodyLarge,
        )
        Text(
            text = "Button",
            modifier = modifier,
            style = Typography.bodyMedium,
        )
        Text(
            text = "Caption",
            modifier = modifier,
            style = Typography.labelSmall,
        )
    }
}

//            Button(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 4.dp, bottom = 4.dp),
//                shape = RoundedCornerShape(8.dp),
//                onClick = { behaviorDetails(context, Intent(context, DisplayBehavior::class.java), behavior["id"] as Int, behavior) },
//                colors = ButtonC.GoldenAmberPrimary,
//                border = borderStroke(C.GoldenAmber),
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(end = 8.dp)
//                    ) {
//                        Text(
//                            text = behavior["name"].toString(),
//                            style = Typography.bodyLarge,
//                        )
//                        Text(
//                            text = behavior["description"].toString(),
//                            style = Typography.labelSmall,
//                        )
//                    }
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
//                        contentDescription = "Fav",
//                        tint = C.Indigo,
//                    )
//                }
//            }

//fun buttonClicked(context: Context, intent: Intent) {
//    context.startActivity(intent)
//}
//
//fun behaviorDetails(context: Context, intent: Intent, id: Int, fullBehavior: Map<String, Any?>) {
//    intent.putExtra("behavior_id", id)
//    intent.putExtra("behavior", JSONObject(fullBehavior).toString())
//    context.startActivity(intent)
//}

//
//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun GoalsDisplay(navController: NavController, modifier: Modifier = Modifier) {
//    LazyRow {
//        items(VariableModel.userGoals) { userGoal ->
//            Button (
//                modifier = modifier
//                    .width(150.dp)
//                    .height(150.dp),
//                border = BorderStroke(2.dp, C.CoralRed),
//                shape = RoundedCornerShape(8.dp),
//                onClick = {
//                    val userGoalString = JSONObject(userGoal).toString()
//                    navController.navigate(route = DisplayGoal(userGoalString))
//                },
//                colors = ButtonC.CoralRedSecondary.copy(
//                    containerColor = Color.White
//                )
//            ) {
//                Text(
//                    text = userGoal["name"] as String,
//                    style = Typography.titleSmall,
//                    textAlign = TextAlign.Center
//                )
//            }
//            Spacer(Modifier.size(8.dp))
//        }
//        item {
//            NewGoalButton({
//                navController.navigate(route = CreateGoal)
//            })
//        }
//    }
//}

//@Composable
//fun Intro(goal: UserGoal) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clip(RoundedCornerShape(bottomEnd = 80.dp, bottomStart = 80.dp))
//            .background(C.CoralRed)
//            .padding(32.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = goal.name,
//            style = Typography.titleLarge,
//        )
//    }
//}
//
//@Composable
//fun BehaviorsDisplayed(navController: NavController, behaviors: MutableList<UserBehaviorWithBehavior>, modifier: Modifier = Modifier) {
//    Column(
//        modifier = modifier.padding(top = 16.dp, bottom = 16.dp)
//    ) {
//        for (behavior in behaviors) {
//            Button(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 4.dp, bottom = 4.dp),
//                shape = RoundedCornerShape(8.dp),
//                onClick = {
//                    val behaviorString = behavior.toJson().toString()
//                    navController.navigate(route = DisplayBehavior(behaviorString))
//                },
//                colors = ButtonC.GoldenAmberPrimary,
//                border = borderStroke(C.GoldenAmber),
//            ) {
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .weight(1f)
//                            .padding(end = 8.dp)
//                    ) {
//                        Text(
//                            text = behavior.behavior.name,
//                            style = Typography.bodyLarge,
//                        )
//                        Text(
//                            text = behavior.behavior.description,
//                            style = Typography.labelSmall,
//                        )
//                    }
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
//                        contentDescription = "Fav",
//                        tint = C.Indigo,
//                    )
//                }
//            }
//        }
//    }
//}