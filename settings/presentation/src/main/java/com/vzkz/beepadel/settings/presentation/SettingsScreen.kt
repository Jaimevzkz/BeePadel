package com.vzkz.beepadel.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.designsystem.components.BeePadelScaffold
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(initial = null)

    LaunchedEffect(events) {
        when (events) {
            null -> {}
            else -> {}
        }
    }

    SettingsScreenRoot(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun SettingsScreenRoot(
    state: SettingsState,
    onAction: (SettingsIntent) -> Unit
) {
    BeePadelScaffold(withGradient = true) {
        Box(
            Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Settings screen", color = MaterialTheme.colorScheme.onBackground)
        }
    }

}

@Preview
@Composable
private fun SettingsScreenPreview() {
    BeePadelTheme {
        SettingsScreenRoot(
            state = SettingsState.initial,
            onAction = {}
        )
    }
}