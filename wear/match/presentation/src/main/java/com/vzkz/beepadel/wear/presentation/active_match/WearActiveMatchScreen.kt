package com.vzkz.beepadel.wear.presentation.active_match

import android.Manifest
import android.content.pm.PackageManager
import android.health.connect.HealthPermissions
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.FilledTonalIconButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.OutlinedButton
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.vzkz.beepadel.designsystem_wear.BeePadelTheme
import com.vzkz.beepadel.wear.presentation.R
import com.vzkz.beepadel.wear.presentation.active_match.WearActiveMatchIntent.*
import com.vzkz.beepadel.wear.presentation.active_match.components.ClickableArea
import com.vzkz.beepadel.wear.presentation.active_match.components.FinishMatchDialog
import com.vzkz.beepadel.wear.presentation.active_match.components.WearServingDialog
import com.vzkz.beepadel.wear.presentation.active_match.components.UndoButton
import com.vzkz.beepadel.wear.presentation.active_match.components.WarningDialog
import com.vzkz.beepadel.wear.presentation.active_match.components.WearScoreCard
import com.vzkz.beepadel.wear.presentation.active_match.model.WearDialogs
import com.vzkz.core.presentation.designsystem.FinishIcon
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

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
        val hasBodySensorPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA)
            perms[HealthPermissions.READ_HEART_RATE] == true
        else
            perms[Manifest.permission.BODY_SENSORS] == true

        onAction(OnBodySensorPermissionResult(hasBodySensorPermission))
    }
    LaunchedEffect(Unit) {
        val hasBodySensorPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
            context.checkSelfPermission(
                HealthPermissions.READ_HEART_RATE
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            context.checkSelfPermission(
                Manifest.permission.BODY_SENSORS
            ) == PackageManager.PERMISSION_GRANTED
        }

        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= 33) {
            context.checkSelfPermission(
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true

        onAction(OnBodySensorPermissionResult(hasBodySensorPermission))

        val permissions = mutableListOf<String>()
        if (!hasBodySensorPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA)
                permissions.add(HealthPermissions.READ_HEART_RATE)
            else
                permissions.add(Manifest.permission.BODY_SENSORS)
        }
        if (!hasNotificationPermission && Build.VERSION.SDK_INT >= 33)
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)

        permissionLauncher.launch(permissions.toTypedArray())
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        ClickableArea(
            modifier = Modifier.fillMaxSize(),
            onAddPointToTeam1 = {
                onAction(AddPointToTeam1)
            },
            onAddPointToTeam2 = {
                onAction(AddPointToTeam2)
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
                onClick = { onAction(ToggleDialog(WearDialogs.FINISH)) },
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
                elapsedTime = state.elapsedTime,
                heartRate = state.heartRate,
                canTrackHeartRate = state.canTrackHeartRate
            )

            UndoButton(onUndoPoint = { onAction(UndoPoint) })
        }

        when (state.dialogToShow) {
            WearDialogs.NONE -> {}

            WearDialogs.SERVING -> {
                WearServingDialog(
                    modifier = Modifier,
                    onStartMatch = { onAction(StartMatch(it)) }
                )
            }

            WearDialogs.FINISH -> {
                FinishMatchDialog(
                    modifier = Modifier,
                    onFinishMatch = { onAction(FinishMatch) },
                    onDiscardMatch = { onAction(DiscardMatch) },
                    onCancel = { onAction(ToggleDialog(WearDialogs.NONE)) }
                )
            }

            WearDialogs.PHONE_NOT_CONNECTED -> {
                WarningDialog(textToDisplay = stringResource(R.string.connect_your_phone))
            }

            WearDialogs.ERROR -> {
                WearErrorDialog(
                    modifier = Modifier,
                    title = stringResource(com.vzkz.match.presentation.R.string.error_occurred),
                    description = state.error?.asString(),
                    primaryButton = {
                        Button(
                            modifier = Modifier
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ),
                            onClick = { onAction(DiscardMatch) }
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                text = stringResource(R.string.discard)
                            )
                        }
                    },
                    secondaryButton = {
                        OutlinedButton(
                            modifier = Modifier
                                .weight(1f),
                            onClick = { onAction(CloseError) }
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                text = stringResource(R.string.cancel)
                            )
                        }
                    }
                )
            }
        }
    }
}


@Composable
internal fun WearErrorDialog(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    primaryButton: @Composable RowScope.() -> Unit,
    secondaryButton: @Composable RowScope.() -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        if (description != null)
            Text(
                text = description,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            secondaryButton()
            primaryButton()

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
                dialogToShow = WearDialogs.NONE,
//                dialogToShow = WearDialogs.SERVING,
//                dialogToShow = WearDialogs.ERROR,
            ),
            onAction = {}
        )
    }
}
