@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.vzkz.beepadel.wear.presentation.active_match


import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.vzkz.beepadel.wear.match.domain.ExerciseTracker
import com.vzkz.beepadel.wear.match.domain.MatchTracker
import com.vzkz.beepadel.wear.match.domain.PhoneConnector
import com.vzkz.beepadel.wear.presentation.active_match.model.WearDialogs
import com.vzkz.core.connectivity.domain.messaging.MessagingAction
import com.vzkz.core.connectivity.domain.messaging.MessagingAction.*
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.presentation.ui.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
import com.vzkz.match.domain.model.Points
import com.vzkz.match.domain.model.toPoints
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


        listenToPhoneActions()
    }

    override fun reduce(intent: WearActiveMatchIntent) {
        sendActionToPhone(intent)
        when (intent) {
            is WearActiveMatchIntent.ToggleDialog -> _state.update { it.copy(dialogToShow = intent.newVal) }
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

            else -> Unit
        }
    }


    private fun sendActionToPhone(intent: WearActiveMatchIntent) {
        viewModelScope.launch {
            val messagingAction = when (intent) {
                is WearActiveMatchIntent.AddPointToTeam1 -> AddPointTo(true)
                WearActiveMatchIntent.AddPointToTeam2 -> AddPointTo(true)
                WearActiveMatchIntent.UndoPoint -> UndoPoint
                is WearActiveMatchIntent.StartMatch -> {
                    Timber.i("Sending start message to phone")
                    Start(intent.isTeam1Serving)
                }
                WearActiveMatchIntent.FinishMatch -> Finish
                WearActiveMatchIntent.DiscardMatch -> Discard
                else -> null
            }
            messagingAction?.let {
                Timber.i("Message action: $messagingAction")
                val result = phoneConnector.sendActionToPhone(it)
                if (result is Result.Error) {
                    Timber.e("Tracker error: ${result.error}")
                }
            }
        }
    }

    private fun listenToPhoneActions() {
        phoneConnector
            .messagingActions
            .onEach { action ->
                when (action) {
                    is Start -> {
                        Timber.i("Receiving start message on watch")
                        exerciseTracker.startExercise()
                        matchTracker.setHasMatchStarted(true)
                        _state.update {
                            it.copy(
                                isTeam1Serving = action.isTeam1Serving,
                                dialogToShow = WearDialogs.NONE
                            )
                        }
                    }

                    Discard -> {
                        matchTracker.setHasMatchStarted(false)
                        exerciseTracker.stopExercise()
                        _state.update { WearActiveMatchState.initial }
                    }

                    Finish -> {
                        matchTracker.setHasMatchStarted(false)
                        exerciseTracker.stopExercise()
                        _state.update { WearActiveMatchState.initial }
                    }

                    is PointsUpdate -> {
                        _state.update {
                            it.copy(
                                pointsTeam1 = action.points.first.toPoints(),
                                pointsTeam2 = action.points.second.toPoints(),
                            )
                        }
                    }

                    is GamesUpdate -> {
                        _state.update {
                            it.copy(
                                pointsTeam1 = Points.Zero,
                                pointsTeam2 = Points.Zero,
                                gamesTeam1 = action.games.first,
                                gamesTeam2 = action.games.second,
                            )
                        }
                    }

                    is UpdateAfterUndo -> {
                        _state.update {
                            it.copy(
                                pointsTeam1 = action.points.first.toPoints(),
                                pointsTeam2 = action.points.second.toPoints(),
                                gamesTeam1 = action.games.first,
                                gamesTeam2 = action.games.second,
                                setsTeam1 = action.sets.first,
                                setsTeam2 = action.sets.second
                            )
                        }
                    }

                    is SetsUpdate -> {
                        _state.update {
                            it.copy(
                                pointsTeam1 = Points.Zero,
                                pointsTeam2 = Points.Zero,
                                gamesTeam1 = 0,
                                gamesTeam2 = 0,
                                setsTeam1 = action.sets.first,
                                setsTeam2 = action.sets.second
                            )
                        }
                    }

                    is ServingUpdate -> _state.update { it.copy(isTeam1Serving = action.isTeam1Serving) }

                    is TimeUpdate -> _state.update { it.copy(elapsedTime = action.elapsedDuration) }
                    else -> Unit
                }
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)
    }
}
