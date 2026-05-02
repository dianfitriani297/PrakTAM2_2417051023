package com.example.praktam2_2417051023.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = GreenSoft,
    background = BackgroundSoft,
    surface = CardSurface,
    onPrimary = OnPrimaryText
)

@Composable
fun ZoopediaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}