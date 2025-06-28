package com.vzkz.match.presentation.match_history

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
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchHistoryScreenRot(
    viewModel: MatchHistoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(initial = null)

    LaunchedEffect(events) {
        when (events) {
            else -> {}
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
    Box(Modifier.fillMaxSize()){
        Text("Match history screen")
    }

}

@Preview
@Composable
private fun MatchHistoryScreenPreview() {
    MatchHistoryScreen(
        state = MatchHistoryState.initial,
        onAction = {}
    )
}