package com.vzkz.match.presentation.active_match

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
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
fun ActiveMatchScreenRot(
    viewModel: ActiveMatchViewmodel = koinViewModel(),
    onNavigateToMatchHistory: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(initial = null)

    LaunchedEffect(events) {
        when (events) {
            ActiveMatchEvent.NavToHistoryScreen -> {
                onNavigateToMatchHistory()
            }

            null -> {}
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
    BeePadelScaffold {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                Text("Active match screen")
                Button(onClick = { onAction(ActiveMatchIntent.FinishMatch) }) {
                    Text("Create match [Debug]")
                }
            }
        }
    }
}

@Preview
@Composable
private fun ActiveMatchScreenPreview() {
    BeePadelTheme {
        ActiveMatchScreen(
            state = ActiveMatchState.initial,
            onAction = {}
        )
    }
}