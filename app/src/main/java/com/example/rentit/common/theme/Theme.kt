package com.example.rentit.common.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
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
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue500,
    onPrimary = White,
    primaryContainer = Gray200,
    onPrimaryContainer = AppBlack,

    secondary = SecondaryYellow,
    onSecondary = White,

    background = White,
    onBackground = AppBlack,

    surface = White,
    onSurface = AppBlack,

    error = AppRed,
    onError = White
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlue500,
    onPrimary = AppBlack,
    primaryContainer = Gray200,
    onPrimaryContainer = AppBlack,

    secondary = SecondaryYellow,
    onSecondary = AppBlack,

    background = AppBlack,
    onBackground = White,

    surface = AppBlack,
    onSurface = White,

    error = AppRed,
    onError = White
)

@Composable
fun RentItTheme(
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
        window.statusBarColor = Color.White.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
      }
    }

    MaterialTheme(
      colorScheme = colorScheme,
      typography = AppTypography,
      content = content
    )
}