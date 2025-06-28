package com.vzkz.core.presentation.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

val DarkColorScheme = darkColorScheme(
    primary = BeePadelBlue,
    background = BeePadelBlack,
    surface = BeePadelDarkGray,
    secondary = BeePadelOrange,
    tertiary = BeePadelWhite,
    primaryContainer = BeePadelBlue30,
    onPrimary = BeePadelBlack,
    onBackground = BeePadelWhite,
    onSurface = BeePadelWhite,
    onSurfaceVariant = BeePadelGray,
    error = BeePadelDarkRed,
    errorContainer = BeePadelDarkRed5
)

@Composable
fun BeePadelTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
