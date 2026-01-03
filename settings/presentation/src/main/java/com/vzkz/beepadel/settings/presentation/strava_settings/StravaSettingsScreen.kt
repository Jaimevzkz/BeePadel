@file:OptIn(ExperimentalMaterial3Api::class)

package com.vzkz.beepadel.settings.presentation.strava_settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vzkz.beepadel.settings.presentation.general_settings.components.BooleanSetting
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.designsystem.components.BeePadelActionButton
import com.vzkz.core.presentation.designsystem.components.BeePadelScaffold
import com.vzkz.common.general.R
import org.koin.androidx.compose.koinViewModel


@Composable
fun StravaSettingsScreen(
    viewModel: StravaSettingsViewmodel = koinViewModel(),
    onNavBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(initial = null)

    LaunchedEffect(events) {
        when (events) {
            null -> {}
            StravaSettingsEvent.NavigateBack -> onNavBack()
        }
    }

    StravaSettingsScreenRoot(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun StravaSettingsScreenRoot(
    state: StravaSettingsState,
    onAction: (StravaSettingsIntent) -> Unit
) {
    BeePadelScaffold(
        topAppBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.configure_strava),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(StravaSettingsIntent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
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
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(12.dp),
        ) {
            Column() {
                BooleanSetting(
                    modifier = Modifier,
                    title = stringResource(R.string.logged_with_beepadel),
                    description = stringResource(R.string.add_a_line_saying_logged_with_beepadel_to_help_the_app_get_more_visibility),
                    value = state.loggedWithBeePadelEnabled,
                    onValueChange = { onAction(StravaSettingsIntent.ToggleLoggedWithBeePadel) }
                )
            }
            BeePadelActionButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                text = stringResource(R.string.logout_from_strava),
                errorButtonColors = true,
                onClick = { onAction(StravaSettingsIntent.LogoutFromStrava) }
            )
        }

    }

}

@Preview
@Composable
private fun StravaSettingsScreenPreview() {
    BeePadelTheme {
        StravaSettingsScreenRoot(
            state = StravaSettingsState.initial,
            onAction = {}
        )
    }
}
