package com.vzkz.match.presentation.match_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
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
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchHistoryScreenRot(
    viewModel: MatchHistoryViewModel = koinViewModel(),
    onNavigateToActiveMatch: (matchId: Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(initial = null)

    LaunchedEffect(events) {
        when (events) {
            is MatchHistoryEvent.NavigateToActiveMatch -> {
                val matchId = (events as MatchHistoryEvent.NavigateToActiveMatch).matchId
                onNavigateToActiveMatch(matchId)
            }

            null -> {}
        }
    }

    MatchHistoryScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun MatchHistoryScreen(
    state: MatchHistoryState,
    onAction: (MatchHistoryIntent) -> Unit
) {
    BeePadelScaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


        }

    }
}

@Preview
@Composable
private fun MatchHistoryScreenPreview() {
    BeePadelTheme {
        MatchHistoryScreen(
            state = MatchHistoryState.initial,
            onAction = {}
        )
    }
}