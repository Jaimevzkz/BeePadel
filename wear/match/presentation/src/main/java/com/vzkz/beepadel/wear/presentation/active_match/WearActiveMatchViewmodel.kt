@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package com.vzkz.beepadel.wear.presentation.active_match


import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.vzkz.beepadel.wear.match.domain.ExerciseTracker
import com.vzkz.beepadel.wear.match.domain.MatchTracker
import com.vzkz.beepadel.wear.match.domain.PhoneConnector
import com.vzkz.beepadel.wear.presentation.active_match.model.WearDialogs
import com.vzkz.core.connectivity.domain.messaging.MessagingAction
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.DataError
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
import com.vzkz.core.domain.error.Result
import com.vzkz.core.presentation.ui.asUiText
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
                if (connectedNode?.isNearby != true)
                    _state.update { it.copy(dialogToShow = WearDialogs.PHONE_NOT_CONNECTED) }
                else if (matchTracker.hasMatchStarted.value)
                    _state.update { it.copy(dialogToShow = WearDialogs.NONE) }
                else{
                    _state.update { it.copy(dialogToShow = WearDialogs.SERVING) }
                }

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
            is WearActiveMatchIntent.ToggleDialog -> {
                _state.update { it.copy(dialogToShow = intent.newVal) }
            }
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

            WearActiveMatchIntent.CloseError -> _state.update { it.copy(dialogToShow = WearDialogs.NONE) }

            else -> Unit
        }
    }


    private fun sendActionToPhone(intent: WearActiveMatchIntent) {
        viewModelScope.launch {
            val messagingAction = when (intent) {
                is WearActiveMatchIntent.AddPointToTeam1 -> MessagingAction.AddPointTo(true)
                WearActiveMatchIntent.AddPointToTeam2 -> MessagingAction.AddPointTo(false)
                WearActiveMatchIntent.UndoPoint -> MessagingAction.UndoPoint
                is WearActiveMatchIntent.StartMatch -> MessagingAction.Start(intent.isTeam1Serving)
                WearActiveMatchIntent.FinishMatch -> MessagingAction.Finish
                WearActiveMatchIntent.DiscardMatch -> MessagingAction.Discard
                WearActiveMatchIntent.CloseError -> MessagingAction.CloseError
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

    private fun listenToPhoneActions() {
        phoneConnector
            .messagingActions
            .onEach { action ->
                when (action) {
                    is MessagingAction.Start -> {
                        exerciseTracker.startExercise()
                        matchTracker.setHasMatchStarted(true)
                        _state.update {
                            it.copy(
                                isTeam1Serving = action.isTeam1Serving,
                                dialogToShow = WearDialogs.NONE
                            )
                        }
                    }

                    MessagingAction.Discard -> {
                        matchTracker.setHasMatchStarted(false)
                        exerciseTracker.stopExercise()
                        _state.update { WearActiveMatchState.initial }
                    }

                    MessagingAction.Finish -> {
                        matchTracker.setHasMatchStarted(false)
                        exerciseTracker.stopExercise()
                        _state.update {
                            WearActiveMatchState.initial
                                .copy(dialogToShow = WearDialogs.MATCH_FINISHED)
                        }
                    }

                    is MessagingAction.PointsUpdate -> {
                        _state.update {
                            it.copy(
                                pointsTeam1 = action.points.first.toPoints(),
                                pointsTeam2 = action.points.second.toPoints(),
                            )
                        }
                    }

                    is MessagingAction.GamesUpdate -> {
                        _state.update {
                            it.copy(
                                pointsTeam1 = Points.Zero,
                                pointsTeam2 = Points.Zero,
                                gamesTeam1 = action.games.first,
                                gamesTeam2 = action.games.second,
                            )
                        }
                    }

                    is MessagingAction.UpdateAfterUndo -> {
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

                    is MessagingAction.SetsUpdate -> {
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

                    is MessagingAction.ServingUpdate -> _state.update { it.copy(isTeam1Serving = action.isTeam1Serving) }

                    is MessagingAction.TimeUpdate -> _state.update { it.copy(elapsedTime = action.elapsedDuration) }

                    MessagingAction.FinishMatchError -> _state.update {
                        it.copy(
                            error = DataError.Logic.EMPTY_SET_LIST.asUiText(),
                            dialogToShow = WearDialogs.ERROR
                        )
                    }

                    MessagingAction.CloseError -> _state.update {
                        it.copy(
                            error = null,
                            dialogToShow = WearDialogs.NONE
                        )
                    }

                    MessagingAction.EnterActiveMatch -> {
                        if (state.value.dialogToShow == WearDialogs.MATCH_FINISHED){
                            _state.update { it.copy(dialogToShow = WearDialogs.SERVING) }
                        }
                    }

                    else -> Unit
                }
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)
    }
}
