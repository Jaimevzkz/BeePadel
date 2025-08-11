@file:OptIn(ExperimentalMaterial3Api::class)

package com.vzkz.beepadel.settings.presentation

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.widget.ToggleButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vzkz.core.presentation.designsystem.BeePadelTheme
import com.vzkz.core.presentation.designsystem.components.BeePadelScaffold
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

    LaunchedEffect(events) {
        when (events) {
            SettingsEvent.NavigateBack -> { onNavigateBack() }
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
            LazyColumn(
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                item {
                    BooleanSetting(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainer),
                        title = stringResource(R.string.golden_point),
                        value = state.goldenPoint,
                        onValueChange = { onAction(SettingsIntent.ToggleGoldenPoint) }
                    )
                }

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
fun BooleanSetting(
    modifier: Modifier = Modifier,
    title: String,
    value: Boolean,
    onValueChange: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title)
        Switch(
            checked = value,
            onCheckedChange = { onValueChange() },
        )
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    BeePadelTheme {
        SettingsScreenRoot(
            state = SettingsState.initial.copy(goldenPoint = true),
            onAction = {}
        )
    }
}