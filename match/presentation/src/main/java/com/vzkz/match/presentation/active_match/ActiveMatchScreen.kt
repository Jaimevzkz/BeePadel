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
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.vzkz.core.domain.Timer
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
import com.vzkz.match.presentation.active_match.components.ActiveMatchDialog
import com.vzkz.match.presentation.active_match.components.ControlsSection
import com.vzkz.match.presentation.active_match.components.CurrentGameScoreCard
import com.vzkz.match.presentation.active_match.components.ServingDialog
import com.vzkz.match.presentation.active_match.components.TopSection
import com.vzkz.match.presentation.active_match.service.ActiveMatchService
import com.vzkz.match.presentation.model.ActiveMatchDialog
import com.vzkz.match.presentation.util.formatted
import com.vzkz.match.presentation.util.hasNotificationPermission
import com.vzkz.match.presentation.util.shouldShowNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import kotlin.math.acos
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
        if (state.isMatchStarted && !ActiveMatchService.isServiceActive) {
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
            if (!state.isMatchStarted && state.showServingDialog) {
                ServingDialog(
                    modifier = Modifier,
                    onStartMatch = { onAction(ActiveMatchIntent.StartMatch(it)) },
                    onCancel = {
                        onAction(ActiveMatchIntent.DiscardMatch)
                    },
                )
            }
            if (state.activeMatchDialogToShow != null) {
                ActiveMatchDialog(
                    activeMatchDialogToShow = state.activeMatchDialogToShow,
                    insertMatchLoading = state.insertMatchLoading,
                    error = state.error,
                    onAction = onAction
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