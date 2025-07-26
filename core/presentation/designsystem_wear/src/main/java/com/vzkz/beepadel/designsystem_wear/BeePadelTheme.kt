package com.vzkz.beepadel.designsystem_wear

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Typography
import com.vzkz.core.presentation.designsystem.DarkColorScheme
import com.vzkz.core.presentation.designsystem.Exo2


private fun createColorScheme(): ColorScheme {
    val phoneTheme = DarkColorScheme
    return ColorScheme(
        primary = phoneTheme.primary,
        primaryContainer = phoneTheme.primaryContainer,
        onPrimary = phoneTheme.onPrimary,
        onPrimaryContainer = phoneTheme.onPrimaryContainer,
        secondary = phoneTheme.secondary,
        onSecondary = phoneTheme.onSecondary,
        secondaryContainer = phoneTheme.secondaryContainer,
        onSecondaryContainer = phoneTheme.onSecondaryContainer,
        tertiary = phoneTheme.tertiary,
        onTertiary = phoneTheme.onTertiary,
        tertiaryContainer = phoneTheme.tertiaryContainer,
        onTertiaryContainer = phoneTheme.onTertiaryContainer,
        surfaceContainer = phoneTheme.surfaceContainer,
        onSurface = phoneTheme.onSurface,
        surfaceContainerLow = phoneTheme.surfaceContainerLow,
        surfaceContainerHigh = phoneTheme.surfaceContainerHigh,
        onSurfaceVariant = phoneTheme.onSurfaceVariant,
        background = phoneTheme.background,
        error = phoneTheme.error,
        onError = phoneTheme.onError,
        onBackground = phoneTheme.onBackground,
    )
}

private fun createTypography(): Typography {
    return Typography(
        defaultFontFamily = Exo2,
    )
}

private val WearColors = createColorScheme()
private val WearTypography = createTypography()

@Composable
fun BeePadelTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WearColors,
        typography = WearTypography
    ) {
        content()
    }

}