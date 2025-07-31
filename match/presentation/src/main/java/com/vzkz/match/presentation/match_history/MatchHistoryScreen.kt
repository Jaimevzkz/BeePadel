@file:OptIn(ExperimentalMaterial3Api::class)

package com.vzkz.match.presentation.match_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vzkz.common.general.data_generator.dummyMatchList
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.designsystem.PadelIcon
import com.vzkz.core.presentation.designsystem.components.BeePadelActionButton
import com.vzkz.core.presentation.designsystem.components.BeePadelDialog
import com.vzkz.core.presentation.designsystem.components.BeePadelFloatingActionButton
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
            BeePadelFloatingActionButton(
                onClick = { onAction(MatchHistoryIntent.NavigateToActiveMatch) },
                icon = PadelIcon,
                contentDescription = stringResource(R.string.start_match)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clip(RoundedCornerShape(12.dp)),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(
                state.matchHistory
            ) { matchUi ->
                MatchCard(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    match = matchUi,
                    onDeleteMatch = {
                        onAction(
                            MatchHistoryIntent.ToggleDeleteDialog(
                                true,
                                matchUi.matchId
                            )
                        )
                    }
                )
            }
        }

        if (state.showDeleteDialog) {
            BeePadelDialog(
                title = stringResource(R.string.permanently_delete),
                onDismiss = {
                    onAction(MatchHistoryIntent.ToggleDeleteDialog(false))
                },
                description = stringResource(R.string.once_deleted_the_match_can_t_be_recovered),
                primaryButton = {
                    BeePadelActionButton(
                        modifier = Modifier.weight(1f),
                        text = stringResource(R.string.confirm),
                        errorButtonColors = true,
                        isLoading = false,
                        onClick = { onAction(MatchHistoryIntent.DeleteMatch) }
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun MatchHistoryScreenPreview() {
    BeePadelTheme {
        MatchHistoryScreen(
            state = MatchHistoryState.initial.copy(
                matchHistory = dummyMatchList().map { it.toMatchUi() },
                showDeleteDialog = true
            ),
            onAction = {}
        )
    }
}