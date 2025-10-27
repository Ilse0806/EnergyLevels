package com.example.microhabits

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.microhabits.components.InformationDetails
import com.example.microhabits.ui.theme.MicroHabitsTheme
import com.example.microhabits.ui.theme.Typography
import org.json.JSONObject


@OptIn(ExperimentalLayoutApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DisplayBehaviorScreen (navController: NavController, receivedBehaviors: DisplayBehavior) {
    val receivedBehaviorsObject = JSONObject(receivedBehaviors.behavior)
    val context = LocalContext.current

    val scrollState = rememberScrollState()
    Scaffold(
        bottomBar = {
            if (!WindowInsets.isImeVisible) {
                Navigation(navController)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState)
        ) {
            BehaviorDetailsShown(receivedBehaviorsObject)
        }
    }
}

@Composable
fun BehaviorDetailsShown(fullBehavior: JSONObject) {
    Column {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp, bottom = 16.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = fullBehavior.get("name").toString(),
                style = Typography.titleMedium,
            )
            if (fullBehavior.get("description").toString().isEmpty()) {
                Text(
                    text = fullBehavior.get("description").toString(),
                    style = Typography.bodyMedium,
                )
            }
        }

        InformationDetails(fullBehavior)
    }
}

@Composable
fun Details(items: JSONObject) {

    Box(
        modifier = Modifier.padding(16.dp)
    ) {
        Column {
            Text(
                text = "${items["title"]}: ${items["text"]}",
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BehaviorPreview() {
    MicroHabitsTheme(dynamicColor = false) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(WindowInsets.safeDrawing.asPaddingValues())) { innerPadding ->
            Column (
                modifier = Modifier.padding(innerPadding)
            ) {
                Column {
//                    Details(listOf(mapOf("title" to "true", "text" to "1", "measured_in" to "amount of times")))
                }
            }
        }
    }
}