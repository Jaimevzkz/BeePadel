package com.vzkz.beepadel.wear.presentation.active_match


import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.vzkz.beepadel.wear.match.domain.ExerciseTracker
import com.vzkz.beepadel.wear.match.domain.PhoneConnector
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.presentation.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class WearActiveMatchViewmodel(
    private val dispatchers: DispatchersProvider,
    private val exerciseTracker: ExerciseTracker,
    private val phoneConnector: PhoneConnector
) :
    BaseViewModel<WearActiveMatchState, WearActiveMatchIntent, WearActiveMatchEvent>(
        WearActiveMatchState.initial,
        dispatchers
    ) {

    private val hasBodySensorPermission = MutableStateFlow(false)

    init {
        phoneConnector
            .connectedNode
            .onEach {connectedNode ->
                _state.update { it.copy(isConnectedPhoneNearBy = connectedNode?.isNearby == true) }
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)

        viewModelScope.launch(dispatchers.default) {
            val isHeartRateTrackingSupported = exerciseTracker.isHeartRateTrackingSupported()
            _state.update { it.copy(canTrackHeartRate = isHeartRateTrackingSupported) }
        }
    }

    override fun reduce(intent: WearActiveMatchIntent) {
        when (intent) {
            WearActiveMatchIntent.AddPointToTeam1 -> TODO()
            WearActiveMatchIntent.AddPointToTeam2 -> TODO()
            WearActiveMatchIntent.DiscardMatch -> TODO()
            WearActiveMatchIntent.FinishMatch -> TODO()
            WearActiveMatchIntent.UndoPoint -> TODO()
            is WearActiveMatchIntent.ToggleDialog -> TODO()
            is WearActiveMatchIntent.OnBodySensorPermissionResult -> {
                hasBodySensorPermission.value = intent.isGranted
                if (intent.isGranted) {
                    viewModelScope.launch(dispatchers.default) {
                        val isHeartRateTrackingSupported =
                            exerciseTracker.isHeartRateTrackingSupported()
                        _state.update { it.copy(canTrackHeartRate = isHeartRateTrackingSupported) }
                    }
                }
            }
        }
    }
}
