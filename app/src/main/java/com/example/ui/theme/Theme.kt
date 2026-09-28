package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkBackground,
    primaryContainer = DarkPrimaryPressed,
    onPrimaryContainer = DarkTextMain,
    secondary = DarkAccent,
    onSecondary = DarkBackground,
    secondaryContainer = DarkElevatedSurface,
    onSecondaryContainer = DarkAccent,
    tertiary = DarkWarning,
    background = DarkBackground,
    onBackground = DarkTextMain,
    surface = DarkSurface,
    onSurface = DarkTextMain,
    surfaceVariant = DarkElevatedSurface,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = DarkDivider
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    primaryContainer = LightBorder,
    onPrimaryContainer = LightPrimaryPressed,
    secondary = LightAccent,
    onSecondary = Color.White,
    secondaryContainer = LightDivider,
    onSecondaryContainer = LightAccent,
    tertiary = LightWarning,
    background = LightBackground,
    onBackground = LightTextMain,
    surface = LightSurface,
    onSurface = LightTextMain,
    surfaceVariant = LightDivider,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    outlineVariant = LightDivider
)

@Composable
fun ShopcialTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
