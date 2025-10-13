@file:OptIn(ExperimentalMaterial3Api::class)

package com.vzkz.beepadel.settings.presentation.strava_settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.beepadel.settings.presentation.R
import com.vzkz.core.presentation.designsystem.components.BeePadelScaffold
import org.koin.androidx.compose.koinViewModel


@Composable
fun StravaSettingsScreen(
    viewModel: StravaSettingsViewmodel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val events by viewModel.events.collectAsState(initial = null)

    LaunchedEffect(events) {
        when (events) {
            null -> {}
            else -> {}
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
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
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
