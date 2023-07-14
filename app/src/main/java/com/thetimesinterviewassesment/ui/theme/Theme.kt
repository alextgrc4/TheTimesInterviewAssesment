package com.thetimesinterviewassesment.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimary, // Status Bar
    secondary = Green,
    background = LightestGreen, // Cell container background
    surface = LightestGreen, // Main Background
    error = Color(0xFFB00020),
    onPrimary = LightestGreen, //
    onSecondary = Color.Red,
    onBackground = Color.Magenta,
    onSurface = Color.Blue,
    onError = Color.White,
    primaryContainer = LightGreen, // Floating button
    secondaryContainer = Color.Green
)

private val LightColorScheme = lightColorScheme(
    primary = LightGreen, // Text
    secondary = Green,
    background = LightestGreen, // Cell container background
    surface = LightestGreen, // Main Background
    error = Color(0xFFB00020),
    onPrimary = LightestGreen,
    onSecondary = Color.Red,
    onBackground = Color.Magenta, //
    onSurface = Color.Blue,
    onError = Color.White,
    primaryContainer = LightGreen, //Floating button
    secondaryContainer = Color.Green
)

@Composable
fun TheTimesInterviewAssesmentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = {
            ProvideTextStyle(
                value = TextStyle(color = Color.White), // Default Text color
                content = content
            )
        }
    )
}