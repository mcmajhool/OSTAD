package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val OstadiColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = Color(0xFF1A1305),
    primaryContainer = GoldContainer,
    onPrimaryContainer = OnGoldContainer,
    secondary = BlueMale,
    onSecondary = Color.White,
    secondaryContainer = NavyContainer,
    onSecondaryContainer = OnNavyContainer,
    tertiary = EmeraldSuccess,
    background = OstadiDarkBg,
    surface = OstadiCardBg,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    surfaceVariant = OstadiCardSurface,
    onSurfaceVariant = DarkTextSecondary,
    outline = OstadiCardBorder,
    error = CrimsonAbsence
)

@Composable
fun OstadiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = OstadiColorScheme,
        typography = Typography,
        content = content
    )
}
