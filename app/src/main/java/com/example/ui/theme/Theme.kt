package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PinkPrimary,
    onPrimary = White,
    primaryContainer = PinkDark,
    onPrimaryContainer = PinkLight,
    secondary = PinkSecondary,
    onSecondary = White,
    tertiary = PinkAccent,
    background = Color(0xFF141214),
    surface = Color(0xFF1E1A1D),
    onBackground = White,
    onSurface = White,
    surfaceVariant = Color(0xFF2E2429),
    onSurfaceVariant = PinkLight,
    outline = PinkDark
)

private val LightColorScheme = lightColorScheme(
    primary = PinkPrimary,
    onPrimary = White,
    primaryContainer = PinkLight,
    onPrimaryContainer = PinkDark,
    secondary = PinkSecondary,
    onSecondary = White,
    secondaryContainer = PinkSoft,
    onSecondaryContainer = PinkDark,
    tertiary = PinkDark,
    background = PinkSoft,
    surface = White,
    onBackground = DarkText,
    onSurface = DarkText,
    surfaceVariant = PinkSoft,
    onSurfaceVariant = TextMuted,
    outline = BorderLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
