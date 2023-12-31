package com.thetimesinterviewassesment

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.thetimesinterviewassesment.ui.theme.DarkestGreen
import com.thetimesinterviewassesment.ui.theme.GreenAppBar
import kotlinx.coroutines.delay

open class BaseActivity : ComponentActivity() {

    @Composable
    fun ErrorAlertDialog(error: String) {
        val activity = (LocalContext.current as? Activity)
        MaterialTheme {
            Column {
                val openDialog = remember { mutableStateOf(true) }
                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = {
                            // Dismiss the dialog when the user clicks outside the dialog or on the back
                            // button. If you want to disable that functionality, simply use an empty
                            // onCloseRequest.
                            openDialog.value = false
                        },
                        title = {
                            Text(text = error, color = DarkestGreen)
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    openDialog.value = false
                                    activity?.finish()
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
                color = GreenAppBar,
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
