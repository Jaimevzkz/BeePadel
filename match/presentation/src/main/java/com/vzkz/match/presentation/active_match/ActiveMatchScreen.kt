package com.vzkz.match.presentation.active_match

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vzkz.core.presentation.designsystem.BallIcon
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.designsystem.Exo2
import com.vzkz.core.presentation.designsystem.PlusOneIcon
import com.vzkz.core.presentation.designsystem.UndoIcon
import com.vzkz.core.presentation.designsystem.components.BeePadelScaffold
import com.vzkz.match.domain.model.Points
import com.vzkz.match.presentation.R
import com.vzkz.match.presentation.util.formatted
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration

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
    BeePadelScaffold(withGradient = false) {
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
            TopSection(
                onDiscardMatchClicked = { onAction(ActiveMatchIntent.DiscardMatch) },
                onEndMatchClicked = { onAction(ActiveMatchIntent.EndMatch) }
            )
            CurrentGameScoreCard(
                modifier = Modifier,
                ownPoints = state.pointsPlayer1,
                otherPoints = state.pointsPlayer2,
                currentOwnGames = state.gamesPlayer1,
                currentOtherGames = state.gamesPlayer2,
                isServing = state.isServing,
                elapsedTime = state.elapsedTime
            )
            Spacer(Modifier)
            ControlsSection(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                ownSets = state.setsPlayer1,
                otherSets = state.setsPlayer2,
                onAddOwnPoint = { onAction(ActiveMatchIntent.AddPointToPlayer1) },
                onAddOtherPoint = { onAction(ActiveMatchIntent.AddPointToPlayer2) },
                onUndo = { onAction(ActiveMatchIntent.UndoPoint) },
            )
            Spacer(Modifier)
        }
    }
}

@Composable
fun TopSection(
    modifier: Modifier = Modifier,
    onDiscardMatchClicked: () -> Unit,
    onEndMatchClicked: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { onDiscardMatchClicked() },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text(stringResource(R.string.discard_match))
        }

        Button(
            onClick = { onEndMatchClicked() },
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(stringResource(R.string.end_match))
        }

    }
}

@Composable
fun ControlsSection(
    modifier: Modifier = Modifier,
    ownSets: Int,
    otherSets: Int,
    onAddOwnPoint: () -> Unit,
    onAddOtherPoint: () -> Unit,
    onUndo: () -> Unit,
) {
    val setFontSize = 36.sp
    val iconButtonPadding = 12.dp
    val iconSize = 40.dp
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(32.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(iconButtonPadding),
                onClick = { onAddOwnPoint() }) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = PlusOneIcon,
                    contentDescription = stringResource(R.string.add_own_point),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Box(
                modifier = Modifier
                    .border(
                        width = 3.dp,
                        brush = Brush.linearGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    modifier = Modifier,
                    text = "$ownSets - $otherSets",
                    fontSize = setFontSize,
                    fontFamily = Exo2
                )
            }

            IconButton(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(iconButtonPadding),
                onClick = { onAddOtherPoint() }) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    imageVector = PlusOneIcon,
                    contentDescription = stringResource(R.string.add_own_point),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }

        }
        IconButton(
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = CircleShape
                )
                .padding(iconButtonPadding),
            onClick = { onUndo() }) {
            Icon(
                modifier = Modifier.size(iconSize),
                imageVector = UndoIcon,
                contentDescription = stringResource(R.string.undo),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }

}

@Composable
fun CurrentGameScoreCard(
    modifier: Modifier = Modifier,
    ownPoints: Points,
    otherPoints: Points,
    currentOwnGames: Int,
    currentOtherGames: Int,
    isServing: Boolean,
    elapsedTime: Duration
) {
    val pointsFontSize = 60.sp
    val gameFontSize = 30.sp
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(12.dp),
            text = elapsedTime.formatted(),
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 30.sp
        )
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            Color.Transparent
                        )
                    )
                )
                .padding(32.dp),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            Text(
                modifier = Modifier,
                text = ownPoints.string,
                fontSize = pointsFontSize,
                fontFamily = Exo2,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.size(24.dp))
            Text(
                modifier = Modifier,
                text = currentOwnGames.toString(),
                fontSize = gameFontSize,
                fontFamily = Exo2,
                color = MaterialTheme.colorScheme.onPrimary
            )
            if (isServing) {
                Spacer(Modifier.size(12.dp))
                Icon(
                    imageVector = BallIcon,
                    contentDescription = stringResource(R.string.own_player_serving),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.secondary,
                        )
                    )
                )
                .padding(32.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End

        ) {
            if (!isServing) {
                Icon(
                    imageVector = BallIcon,
                    contentDescription = stringResource(R.string.own_player_serving),
                    tint = MaterialTheme.colorScheme.onSecondary
                )
                Spacer(Modifier.size(12.dp))
            }
            Text(
                modifier = Modifier,
                text = currentOtherGames.toString(),
                fontSize = gameFontSize,
                fontFamily = Exo2,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(Modifier.size(24.dp))
            Text(
                modifier = Modifier,
                text = otherPoints.string,
                fontSize = pointsFontSize,
                fontFamily = Exo2,
                color = MaterialTheme.colorScheme.onSecondary
            )

        }
    }

}

@Preview
@Composable
private fun ActiveMatchScreenPreview() {
    BeePadelTheme {
        ActiveMatchScreen(
            state = ActiveMatchState.initial.copy(
                setsPlayer1 = 1,
                gamesPlayer1 = 5,
                pointsPlayer1 = Points.Forty,
                setsPlayer2 = 1,
                gamesPlayer2 = 3,
                pointsPlayer2 = Points.Fifteen
            ),
            onAction = {}
        )
    }
}