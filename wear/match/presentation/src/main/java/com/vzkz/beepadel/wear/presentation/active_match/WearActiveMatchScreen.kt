package com.vzkz.beepadel.wear.presentation.active_match

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.vzkz.beepadel.designsystem_wear.BeePadelTheme
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
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Text("Hello World!", color = Color.White)
    }

}

@WearPreviewDevices
@Composable
private fun WearActiveMatchScreenPreview() {
    BeePadelTheme {
        WearActiveMatchScreen(
            state = WearActiveMatchState.initial,
            onAction = {}
        )
    }
}
