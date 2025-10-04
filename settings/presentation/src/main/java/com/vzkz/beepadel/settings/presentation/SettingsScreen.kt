@file:OptIn(ExperimentalMaterial3Api::class)

package com.vzkz.beepadel.settings.presentation

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.widget.ToggleButton
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vzkz.core.presentation.designsystem.BeePadelDarkRed
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.designsystem.StravaIcon
import com.vzkz.core.presentation.designsystem.components.BeePadelScaffold
import com.vzkz.core.presentation.ui.components.applyIf
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import org.koin.androidx.compose.koinViewModel
import org.koin.core.module.flatten
import timber.log.Timber


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(initial = null)
    val activity = LocalActivity.current

    BackHandler {
        onNavigateBack()
    }

    LaunchedEffect(events) {
        when (events) {
            SettingsEvent.NavigateBack -> {
                onNavigateBack()
            }

            is SettingsEvent.LaunchAuthRequestIntent -> {
                activity?.startActivity((events as SettingsEvent.LaunchAuthRequestIntent).intent)

            }

            null -> {}
        }
    }

    SettingsScreenRoot(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun SettingsScreenRoot(
    state: SettingsState,
    onAction: (SettingsIntent) -> Unit
) {
    BeePadelScaffold(
        topAppBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(SettingsIntent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.nav_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent
                ),

                )
        },
        withGradient = false
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 8.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .scrollable(rememberScrollState(), Orientation.Vertical)
            ) {
                SectionTitle(
                    modifier = Modifier,
                    text = stringResource(R.string.features)
                )

                BooleanSetting(
                    modifier = Modifier,
                    title = stringResource(R.string.golden_point),
                    value = state.goldenPoint,
                    onValueChange = { onAction(SettingsIntent.ToggleGoldenPoint) }
                )

                SectionTitle(
                    modifier = Modifier,
                    text = stringResource(R.string.connect)
                )
                ClickableSetting(
                    modifier = Modifier,
                    icon = StravaIcon,
                    iconContentDescription = stringResource(R.string.connect_with_strava),
                    title = if (state.isLoggedIntoStrava) stringResource(R.string.connected_to_strava) else stringResource(
                        R.string.connect_with_strava
                    ),
                    secondaryAction =
                        if (state.isLoggedIntoStrava) {
                            {
                                OutlinedButton(
                                    border = BorderStroke(1.dp, BeePadelDarkRed),
                                    onClick = {},
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = BeePadelDarkRed,

                                        )
                                ) {
                                    Text("Disconnect")
                                }
                            }
                        } else null,
                    onClick = { onAction(SettingsIntent.LaunchAuthRequestIntent) }
                )
            }

            Text(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                text = "version ${BuildConfig.APP_VERSION_NAME}",
                color = MaterialTheme.colorScheme.onBackground
            )


        }
    }

}

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier.padding(vertical = 8.dp),
        text = text,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
fun BooleanSetting(
    modifier: Modifier = Modifier,
    title: String,
    value: Boolean,
    onValueChange: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = MaterialTheme.colorScheme.onSurface)
        Switch(
            checked = value,
            onCheckedChange = { onValueChange() },
        )
    }
}

@Composable
fun ClickableSetting(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconContentDescription: String? = null,
    title: String,
    secondaryAction: (@Composable () -> Unit)?,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable(enabled = secondaryAction == null) {
                onClick()
            }
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(horizontal = 12.dp, vertical = 26.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(20.dp),
                imageVector = icon,
                contentDescription = iconContentDescription
            )
        }
        Text(text = title, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.weight(1f))
        if (secondaryAction != null) {
            secondaryAction()
        } else
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null
            )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    BeePadelTheme {
        SettingsScreenRoot(
            state = SettingsState.initial.copy(goldenPoint = true, isLoggedIntoStrava = true),
            onAction = {}
        )
    }
}