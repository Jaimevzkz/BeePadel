package com.vzkz.match.presentation.active_match

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.vzkz.core.presentation.designsystem.components.BeePadelActionButton
import com.vzkz.core.presentation.designsystem.components.BeePadelDialog
import com.vzkz.core.presentation.designsystem.components.BeePadelOutlinedActionButton
import com.vzkz.core.presentation.designsystem.components.BeePadelScaffold
import com.vzkz.match.domain.model.Points
import com.vzkz.match.presentation.R
import com.vzkz.match.presentation.active_match.service.ActiveMatchService
import com.vzkz.match.presentation.model.ActiveMatchDialog
import com.vzkz.match.presentation.util.formatted
import com.vzkz.match.presentation.util.hasNotificationPermission
import com.vzkz.match.presentation.util.shouldShowNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel
import kotlin.time.Duration

@Composable
fun ActiveMatchScreenRot(
    viewModel: ActiveMatchViewmodel = koinViewModel(),
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
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
        onAction = viewModel::onAction,
        onServiceToggle = onServiceToggle
    )
}

@Composable
private fun ActiveMatchScreen(
    state: ActiveMatchState,
    onServiceToggle: (isServiceRunning: Boolean) -> Unit,
    onAction: (ActiveMatchIntent) -> Unit
) {

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= 33) {
            perms[Manifest.permission.POST_NOTIFICATIONS] == true
        } else true

        val activity = context as ComponentActivity
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveMatchIntent.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = hasNotificationPermission,
                showNotificationPermissionRationale = showNotificationRationale
            )
        )
    }

    LaunchedEffect(Unit) {
        val activity = context as ComponentActivity
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveMatchIntent.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationPermissionRationale = showNotificationRationale
            )
        )

        if (!showNotificationRationale) {
            permissionLauncher.requestBeepadelPermissions(context)
        }
    }

    LaunchedEffect(key1 = state.isMatchFinished) {
        if (state.isMatchFinished) {
            onServiceToggle(false)
        }
    }

    LaunchedEffect(key1 = state.isMatchStarted) {
        if (!ActiveMatchService.isServiceActive) {
            onServiceToggle(true)
        }
    }

    BeePadelScaffold(withGradient = false) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                TopSection(
                    onDiscardMatchClicked = {
                        onAction(
                            ActiveMatchIntent.ShowActiveDialog(
                                ActiveMatchDialog.DISCARD_MATCH
                            )
                        )
                    },
                    onEndMatchClicked = {
                        onAction(
                            ActiveMatchIntent.ShowActiveDialog(
                                ActiveMatchDialog.FINISH_MATCH
                            )
                        )
                    }
                )

                CurrentGameScoreCard(
                    modifier = Modifier,
                    ownPoints = state.pointsPlayer1,
                    otherPoints = state.pointsPlayer2,
                    currentOwnGames = state.gamesPlayer1,
                    currentOtherGames = state.gamesPlayer2,
                    isServing = state.isTeam1Serving,
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
            if (!state.isMatchStarted) {
                ServingDialog(
                    modifier = Modifier,
                    onStartMatch = { onAction(ActiveMatchIntent.StartMatch(it)) },
                    onCancel = {
                        onAction(ActiveMatchIntent.NavToHistoryScreen)
                    },
                )
            }
            if (state.activeMatchDialogToShow != null) {
                val activeDialogTitle: Int
                val onClickIntent: ActiveMatchIntent
                val errorButtonColor: Boolean
                val activeDialogDescription: String?
                val primaryButtonText: String
                when (state.activeMatchDialogToShow) {
                    ActiveMatchDialog.DISCARD_MATCH -> {
                        activeDialogTitle = R.string.discard_match_question
                        onClickIntent = ActiveMatchIntent.DiscardMatch
                        errorButtonColor = true
                        activeDialogDescription = null
                        primaryButtonText = stringResource(R.string.discard)
                    }

                    ActiveMatchDialog.FINISH_MATCH -> {
                        activeDialogTitle = R.string.end_match_question
                        onClickIntent = ActiveMatchIntent.FinishMatch
                        errorButtonColor = false
                        activeDialogDescription = null
                        primaryButtonText = stringResource(R.string.end)
                    }

                    ActiveMatchDialog.ERROR -> {
                        activeDialogTitle = R.string.error_occurred
                        onClickIntent = ActiveMatchIntent.DiscardMatch
                        errorButtonColor = true
                        activeDialogDescription = state.error?.asString()
                        primaryButtonText = stringResource(R.string.discard)
                    }
                }

                BeePadelDialog(
                    modifier = Modifier,
                    title = stringResource(activeDialogTitle),
                    description = activeDialogDescription,
                    onDismiss = {
                        onAction(ActiveMatchIntent.CloseActiveDialog)
                    },
                    primaryButton = {
                        BeePadelActionButton(
                            modifier = Modifier.weight(1f),
                            text = primaryButtonText,
                            isLoading = state.insertMatchLoading,
                            errorButtonColors = errorButtonColor,
                            onClick = {
                                onAction(onClickIntent)
                            }
                        )
                    },
                    secondaryButton = {
                        BeePadelOutlinedActionButton(
                            modifier = Modifier.weight(1f),
                            text = stringResource(R.string.cancel),
                            onClick = {
                                onAction(ActiveMatchIntent.CloseActiveDialog)
                            }
                        )
                    }
                )
            }
            if (state.showNotificationRationale) {
                BeePadelDialog(
                    title = stringResource(id = R.string.permission_required),
                    onDismiss = { /* no-op */ },
                    description = when {

                        else -> {
                            stringResource(id = R.string.notification_rationale)
                        }
                    },
                    primaryButton = {
                        BeePadelOutlinedActionButton(
                            text = stringResource(id = R.string.okay),
                            onClick = {
                                onAction(ActiveMatchIntent.DismissRationaleDialog)
                                permissionLauncher.requestBeepadelPermissions(context)
                            },
                        )
                    }
                )
            }
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
    val iconButtonPadding = 16.dp
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
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onAddOwnPoint() }
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(iconButtonPadding)
            ) {
                Icon(
                    modifier = Modifier
                        .size(iconSize),
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

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onAddOtherPoint() }
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(iconButtonPadding)
            ) {
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
    isServing: Boolean?,
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
                .then(
                    if (isServing == true)
                        Modifier.border(
                            2.dp, Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.onPrimary,
                                    Color.Transparent
                                )
                            ), RoundedCornerShape(8.dp)
                        )
                    else Modifier
                )
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
            if (isServing == true) {
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
                .then(
                    if (isServing == false)
                        Modifier.border(
                            2.dp, Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.onSecondary
                                )
                            ), RoundedCornerShape(8.dp)
                        )
                    else Modifier
                )
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
            if (isServing == false) {
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

@Composable
fun ServingDialog(
    modifier: Modifier = Modifier,
    onStartMatch: (team1Serving: Boolean) -> Unit,
    onCancel: () -> Unit
) {
    var team1Serving by remember { mutableStateOf(true) }
    BeePadelDialog(
        modifier = modifier,
        title = stringResource(R.string.who_starts_serving),
        onDismiss = {},
        body = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    RadioButton(
                        selected = team1Serving,
                        onClick = { team1Serving = true },
                    )
                    Text(
                        text = stringResource(R.string.team_1),
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    RadioButton(
                        selected = !team1Serving,
                        onClick = { team1Serving = false },
                    )
                    Text(
                        text = stringResource(R.string.team_2),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        },
        primaryButton = {
            BeePadelActionButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.start),
                isLoading = false,
                onClick = {
                    onStartMatch(team1Serving)
                }
            )
        },
        secondaryButton = {
            BeePadelOutlinedActionButton(
                modifier = Modifier.weight(1f),
                text = stringResource(R.string.cancel),
                isLoading = false,
                onClick = {
                    onCancel()
                }
            )
        }
    )
}

private fun ActivityResultLauncher<Array<String>>.requestBeepadelPermissions(
    context: Context
) {
    val hasNotificationPermission = context.hasNotificationPermission()

    val notificationPermission = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else arrayOf()

    when {
        !hasNotificationPermission -> launch(notificationPermission)
    }
}

@Preview
@Composable
private fun ActiveMatchScreenPreview() {
    BeePadelTheme {
        ActiveMatchScreen(
            state = ActiveMatchState.initial.copy(
//                setsPlayer1 = 1,
//                gamesPlayer1 = 5,
//                pointsPlayer1 = Points.Forty,
//                setsPlayer2 = 1,
//                gamesPlayer2 = 3,
//                pointsPlayer2 = Points.Fifteen,
                isTeam1Serving = true
            ),
            onAction = {},
            onServiceToggle = {}
        )
    }
}