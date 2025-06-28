package com.vzkz.active_match.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActiveMatchScreenRot(
    viewModel: ActiveMatchViewmodel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(initial = null)

    LaunchedEffect(events) {
        when (events) {
            else -> {}
        }
    }

    ActiveMatchScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ActiveMatchScreen(
    state: ActiveMatchState,
    onAction: (ActiveMatchIntent) -> Unit
) {
    Box(Modifier.fillMaxSize()){
        Text("Active match screen")
    }
}

@Preview
@Composable
private fun ActiveMatchScreenPreview() {
    ActiveMatchScreen(
        state = ActiveMatchState.initial,
        onAction = {}
    )
}