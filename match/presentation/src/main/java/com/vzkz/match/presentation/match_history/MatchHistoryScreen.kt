@file:OptIn(ExperimentalMaterial3Api::class)

package com.vzkz.match.presentation.match_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vzkz.common.matchList
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.designsystem.PadelIcon
import com.vzkz.core.presentation.designsystem.components.BeePadelScaffold
import com.vzkz.match.presentation.R
import com.vzkz.match.presentation.match_history.components.MatchCard
import com.vzkz.match.presentation.util.toMatchUi
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MatchHistoryScreenRot(
    viewModel: MatchHistoryViewModel = koinViewModel(),
    onNavigateToActiveMatch: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(initial = null)

    LaunchedEffect(events) {
        when (events) {
            is MatchHistoryEvent.NavigateToActiveMatch -> {
                onNavigateToActiveMatch()
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
    BeePadelScaffold(
        topAppBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.beepadel),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {onAction(MatchHistoryIntent.NavigateToActiveMatch)},
                content = {
                    Icon(
                        imageVector = PadelIcon,
                        contentDescription = stringResource(R.string.start_match)
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                state.matchHistory
            ) { matchUi ->
                MatchCard(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    match = matchUi
                )
            }
        }
    }
}

@Preview
@Composable
private fun MatchHistoryScreenPreview() {
    BeePadelTheme {
        MatchHistoryScreen(
            state = MatchHistoryState.initial.copy(matchHistory = matchList().map { it.toMatchUi() }),
            onAction = {}
        )
    }
}