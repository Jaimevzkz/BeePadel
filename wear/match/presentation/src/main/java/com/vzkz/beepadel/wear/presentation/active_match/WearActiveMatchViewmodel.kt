@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.vzkz.beepadel.wear.presentation.active_match


import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Paint
import androidx.lifecycle.viewModelScope
import com.vzkz.beepadel.wear.match.domain.ExerciseTracker
import com.vzkz.beepadel.wear.match.domain.MatchTracker
import com.vzkz.beepadel.wear.match.domain.PhoneConnector
import com.vzkz.core.connectivity.domain.messaging.MessagingAction
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.presentation.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import com.vzkz.core.domain.error.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber

class WearActiveMatchViewmodel(
    private val dispatchers: DispatchersProvider,
    private val exerciseTracker: ExerciseTracker,
    private val phoneConnector: PhoneConnector,
    private val matchTracker: MatchTracker
) :
    BaseViewModel<WearActiveMatchState, WearActiveMatchIntent, WearActiveMatchEvent>(
        WearActiveMatchState.initial,
        dispatchers
    ) {

    private val hasBodySensorPermission = MutableStateFlow(false)

    private val phoneNearbyFlow = snapshotFlow {
        state.value.isConnectedPhoneNearBy
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    val isAmbientMode = snapshotFlow { state.value.isAmbientMode }

    init {
        phoneConnector
            .connectedNode
            .onEach { connectedNode ->
                _state.update { it.copy(isConnectedPhoneNearBy = connectedNode?.isNearby == true) }
                phoneConnector.sendActionToPhone(MessagingAction.ConnectionRequest)
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)

        viewModelScope.launch(dispatchers.default) {
            val isHeartRateTrackingSupported = exerciseTracker.isHeartRateTrackingSupported()
            _state.update { it.copy(canTrackHeartRate = isHeartRateTrackingSupported) }
        }

//        phoneNearbyFlow // todo not sure this will work as expected --> PROBABLY does not work as expected
//            .onEach { isPhoneNerby ->
//                val result = when {
////                    isPhoneNerby && !state.value.hasMatchStarted -> { //todo WHERE to start the exercise???
////                        exerciseTracker.startExercise()
////                    }
//
//                    isPhoneNerby && !state.value.isMatchResumed -> {
//                        exerciseTracker.resumeExercise()
//                    }
//
//                    !isPhoneNerby && state.value.isMatchResumed -> {
//                        exerciseTracker.pauseExercise()
//                    }
//
//                    else -> Result.Success(Unit)
//                }
//                if (result is Result.Error) {
//                    //todo send error (however i want to handle it)
//                }
//
////                matchTracker.setIsTracking(isTracking)
//            }
//            .launchIn(viewModelScope)

        isAmbientMode
            .flatMapLatest { isAmbientModeActive ->
                if (isAmbientModeActive) {
                    matchTracker
                        .hearRate
                        .sample(10.seconds)
                } else matchTracker.hearRate
            }
            .onEach { newHeartRate ->
                _state.update { it.copy(heartRate = newHeartRate) }
            }
            .launchIn(viewModelScope)

        isAmbientMode
            .flatMapLatest { isAmbientModeActive ->
                if (isAmbientModeActive) {
                    matchTracker
                        .elapsedTime
                        .sample(10.seconds)
                } else matchTracker.elapsedTime
            }
            .onEach { elapsedTime ->
                _state.update { it.copy(elapsedTime = elapsedTime) }
            }
            .launchIn(viewModelScope)

        // todo receive values form tracker

        //todo listenToPhoneActions()
    }

    override fun reduce(intent: WearActiveMatchIntent) {
        // todo how to avoid circular dependency -> add triggeredOnPhone on this method but how to not alter other viewmodel?
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


    private fun sendActionToPhone(action: WearActiveMatchIntent) {
        viewModelScope.launch {
            val messagingAction = when (action) {
                is WearActiveMatchIntent.AddPointToTeam1 -> MessagingAction.PointsUpdate(0 to 0) //todo
                else -> null

            }
            messagingAction?.let {
                val result = phoneConnector.sendActionToPhone(it)
                if (result is Result.Error) {
                    Timber.e("Tracker error: ${result.error}")
                }
            }
        }
    }
}
