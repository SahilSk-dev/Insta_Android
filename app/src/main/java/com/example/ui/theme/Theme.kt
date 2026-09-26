package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = InstagramBlue,
    onPrimary = Color.White,
    secondary = InstagramBubblePurple,
    onSecondary = Color.White,
    tertiary = InstagramRed,
    background = InstagramBlack,
    onBackground = Color.White,
    surface = InstagramBlack,
    onSurface = Color.White,
    surfaceVariant = InstagramInputBg,
    onSurfaceVariant = InstagramPlaceholder,
  )

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content
  )
}

