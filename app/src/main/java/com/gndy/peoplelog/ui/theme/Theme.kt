package com.gndy.peoplelog.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Indigo600,
    onPrimary = White,
    primaryContainer = Indigo50,
    onPrimaryContainer = Indigo700,
    
    secondary = Slate600,
    onSecondary = White,
    secondaryContainer = Slate50,
    onSecondaryContainer = Slate900,
    
    background = White,
    surface = White,
    onBackground = Slate950,
    onSurface = Slate900,
    
    error = Rose500,
    onError = White,
    outline = Slate200,
    surfaceVariant = Slate50,
    onSurfaceVariant = Slate600
)

// Dark theme for production readiness
private val DarkColorScheme = darkColorScheme(
    primary = Indigo500,
    onPrimary = White,
    primaryContainer = Slate800,
    onPrimaryContainer = Indigo100,
    
    secondary = Slate400,
    onSecondary = White,
    secondaryContainer = Slate950,
    onSecondaryContainer = Slate200,
    
    background = Slate950,
    surface = Slate900,
    onBackground = Slate50,
    onSurface = Slate50,
    
    error = Rose500,
    outline = Slate600
)

@Composable
fun PeopleLogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
