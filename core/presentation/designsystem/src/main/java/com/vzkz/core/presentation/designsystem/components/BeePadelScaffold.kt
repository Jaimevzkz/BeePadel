package com.vzkz.core.presentation.designsystem.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier

@Composable
fun BeePadelScaffold(
    modifier: Modifier = Modifier,
    withGradient: Boolean = true,
    topAppBar: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = FabPosition.Center,
        topBar = topAppBar
    ) { innerPadding ->
        if (withGradient) {
            GradientBackground {
                content(innerPadding)
            }
        } else {
            content(innerPadding)
        }
    }
}