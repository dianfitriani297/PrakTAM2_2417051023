package com.example.praktam2_2417051023.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = GreenSoft,
    tertiary = OrangeButton,
    background = BackgroundSoft,
    surface = CardSurface,
    error = SalahColor,
    onPrimary = OnPrimaryText,
    onSecondary = GreenPrimary,
    onTertiary = OnPrimaryText,
    onBackground = GreenPrimary,
    onSurface = GreenPrimary,
    onError = OnPrimaryText
)

@Composable
fun ZoopediaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}