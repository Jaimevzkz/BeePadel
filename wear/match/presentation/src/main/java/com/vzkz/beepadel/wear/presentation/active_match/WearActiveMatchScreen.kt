package com.vzkz.beepadel.wear.presentation.active_match

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.FilledTonalIconButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedIconButton
import androidx.wear.compose.material3.RadioButton
import androidx.wear.compose.material3.RadioButtonDefaults
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.vzkz.beepadel.designsystem_wear.BeePadelTheme
import com.vzkz.beepadel.wear.presentation.active_match.components.ClickableArea
import com.vzkz.beepadel.wear.presentation.active_match.components.FinishMatchDialog
import com.vzkz.beepadel.wear.presentation.active_match.components.UndoButton
import com.vzkz.beepadel.wear.presentation.active_match.components.WarningScreen
import com.vzkz.beepadel.wear.presentation.active_match.components.WearScoreCard
import com.vzkz.beepadel.wear.presentation.active_match.model.WearDialogs
import com.vzkz.core.presentation.designsystem.FinishIcon
import com.vzkz.core.presentation.designsystem.StartIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun WearActiveMatchScreenRoot(
    viewModel: WearActiveMatchViewmodel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(initial = null)

    LaunchedEffect(events) {
        when (events) {
            null -> {}
            else -> {}
        }
    }

    WearActiveMatchScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun WearActiveMatchScreen(
    state: WearActiveMatchState,
    onAction: (WearActiveMatchIntent) -> Unit
) {

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { perms ->
        val hasBodySensorPermission = perms[Manifest.permission.BODY_SENSORS] == true
        onAction(WearActiveMatchIntent.OnBodySensorPermissionResult(hasBodySensorPermission))
    }
    LaunchedEffect(Unit) {
        val hasBodySensorPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BODY_SENSORS
        ) == PackageManager.PERMISSION_GRANTED
        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= 33) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true

        onAction(WearActiveMatchIntent.OnBodySensorPermissionResult(hasBodySensorPermission))

        val permissions = mutableListOf<String>()
        if (!hasBodySensorPermission) {
            permissions.add(Manifest.permission.BODY_SENSORS)
        }
        if (!hasNotificationPermission && Build.VERSION.SDK_INT >= 33) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }
        permissionLauncher.launch(permissions.toTypedArray())
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ClickableArea(
            modifier = Modifier,
            onAddPointToTeam1 = {
                onAction(WearActiveMatchIntent.AddPointToTeam1)
            },
            onAddPointToTeam2 = {
                onAction(WearActiveMatchIntent.AddPointToTeam1)
            }
        )

        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(
                4.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilledTonalIconButton(
                onClick = { onAction(WearActiveMatchIntent.ToggleDialog(WearDialogs.FINISH)) },
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Icon(
                    imageVector = FinishIcon,
                    contentDescription = ""
                )
            }

            WearScoreCard(
                pointsTeam1 = state.pointsTeam1,
                gamesTeam1 = state.gamesTeam1,
                pointsTeam2 = state.pointsTeam2,
                gamesTeam2 = state.gamesTeam2,
                isTeam1Serving = state.isTeam1Serving,
                setsTeam1 = state.setsTeam1,
                setsTeam2 = state.setsTeam2,
                elapsedTime = state.elapsedTime
            )

            UndoButton(onUndoPoint = { onAction(WearActiveMatchIntent.UndoPoint) })
        }

        when (state.dialogToShow) {
            WearDialogs.NONE -> {/*No-Op*/
            }

            WearDialogs.SERVING -> {
                ServingDialog(
                    modifier = Modifier,
                    onStartMatch ={onAction(WearActiveMatchIntent.StartMatch(it))}
                )
            }

            WearDialogs.FINISH -> {
                FinishMatchDialog(
                    modifier = Modifier,
                    onFinishMatch = { onAction(WearActiveMatchIntent.FinishMatch) },
                    onDiscardMatch = { onAction(WearActiveMatchIntent.DiscardMatch) },
                    onCancel = { onAction(WearActiveMatchIntent.ToggleDialog(WearDialogs.NONE)) }
                )
            }

            WearDialogs.PHONE_NOT_CONNECTED -> {
                WarningScreen(textToDisplay = stringResource(com.vzkz.beepadel.wear.presentation.R.string.connect_your_phone))
            }
        }
    }
}

@Composable
fun ServingDialog(
    modifier: Modifier = Modifier,
    onStartMatch: (isTeam1Serving: Boolean) -> Unit
) {
    var servingTeam1 by remember { mutableStateOf(true) }
    val radioButtonColors = RadioButtonDefaults.radioButtonColors(
        selectedControlColor = MaterialTheme.colorScheme.onPrimary,
        selectedContentColor = MaterialTheme.colorScheme.onPrimary,
        selectedContainerColor = MaterialTheme.colorScheme.primary,
        unselectedContentColor = MaterialTheme.colorScheme.onSurface,
        unselectedContainerColor = MaterialTheme.colorScheme.surfaceContainer
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp, alignment = Alignment.CenterVertically)
    ) {
        Text(modifier = Modifier.padding(top = 28.dp), text = stringResource(com.vzkz.beepadel.wear.presentation.R.string.who_starts_serving), color = MaterialTheme.colorScheme.onBackground)
        RadioButton(
            modifier = Modifier.height(34.dp),
            selected = servingTeam1,
            onSelect = { servingTeam1 = true },
            label = { Text(stringResource(com.vzkz.match.presentation.R.string.team_1)) },
            colors = radioButtonColors
        )

        RadioButton(
            modifier = Modifier.height(34.dp),
            selected = !servingTeam1,
            onSelect = { servingTeam1 = false },
            label = { Text(stringResource(com.vzkz.match.presentation.R.string.team_2)) },
           colors = radioButtonColors.copy(
               selectedControlColor = MaterialTheme.colorScheme.onSecondary,
               selectedContentColor = MaterialTheme.colorScheme.onSecondary,
               selectedContainerColor = MaterialTheme.colorScheme.secondary,
           )
        )

        OutlinedIconButton(
            onClick = {onStartMatch(servingTeam1)},
            modifier = Modifier
        ) {
            Icon(
                imageVector = StartIcon,
                contentDescription = stringResource(id = com.vzkz.match.presentation.R.string.start_match),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@WearPreviewDevices
@Composable
private fun WearActiveMatchScreenPreview() {
    BeePadelTheme {
        WearActiveMatchScreen(
            state = WearActiveMatchState.initial.copy(
                setsTeam1 = 3,
                setsTeam2 = 2,
                isTeam1Serving = true,
//                dialogToShow = WearDialogs.SERVING,
                dialogToShow = WearDialogs.NONE,
            ),
            onAction = {}
        )
    }
}
