package com.thetimesinterviewassesment.model

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.thetimesinterviewassesment.ui.theme.DarkestGreen

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