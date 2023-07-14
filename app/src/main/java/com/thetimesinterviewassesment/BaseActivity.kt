package com.thetimesinterviewassesment

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thetimesinterviewassesment.ui.theme.DarkGreen
import com.thetimesinterviewassesment.ui.theme.Green
import com.thetimesinterviewassesment.ui.theme.GreenAppBar
import com.thetimesinterviewassesment.ui.theme.LightGreen
import kotlinx.coroutines.delay

open class BaseActivity : ComponentActivity() {

    @Composable
    fun ErrorAlertDialog(error: String) {
        MaterialTheme {
            Column {
                val openDialog = remember { mutableStateOf(false) }

                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            // Dismiss the dialog when the user clicks outside the dialog or on the back
                            // button. If you want to disable that functionality, simply use an empty
                            // onCloseRequest.
                            openDialog.value = false
                        },
                        title = {
                            Text(text = error)
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    openDialog.value = false
                                }) {
                                Text("OK")
                            }
                        }
                    )
                }
            }

        }
    }

    @Composable
    fun ProgressIndicator(enabled: Boolean) {

        var enabled by remember { mutableStateOf(enabled) }
        var progress by remember { mutableStateOf(0.0f) }
        val animatedProgress by animateFloatAsState(
            visibilityThreshold = 0.001f,
            targetValue = progress,

            )

        LaunchedEffect(enabled) {
            while ((progress < 1) && enabled) {
                progress += 0.005f
                delay(10)
            }
        }

        if (progress >= 1f) {
            enabled = false
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Loading...",
                color = Green,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 60.dp, vertical = 5.dp)

            )
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(10.dp)
                    .clip(RoundedCornerShape(20.dp)), // Rounded edges
            )
        }
    }
}
