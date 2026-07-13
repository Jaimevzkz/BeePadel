package com.vzkz.match.presentation.active_match

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.vzkz.core.connectivity.domain.messaging.MessagingAction
import com.vzkz.core.connectivity.domain.messaging.MessagingAction.ConnectionRequest
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.core.notification.ActiveMatchService
import com.vzkz.core.presentation.ui.BaseViewModel
import com.vzkz.common.general.R
import com.vzkz.core.presentation.ui.asUiText
import com.vzkz.match.domain.MatchTracker
import com.vzkz.match.domain.WatchConnector
import com.vzkz.match.presentation.model.ActiveMatchDialog
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class ActiveMatchViewmodel(
    private val dispatchers: DispatchersProvider,
    private val matchTracker: MatchTracker,
    private val watchConnector: WatchConnector,
) :
    BaseViewModel<ActiveMatchState, ActiveMatchIntent, ActiveMatchEvent>(
        ActiveMatchState.initial,
        dispatchers
    ) {

    init {
        _state.update { it.copy(isMatchStarted = ActiveMatchService.isServiceActive.value) }
        ioLaunch {
            watchConnector.sendActionToWatch(MessagingAction.EnterActiveMatch)
        }

        matchTracker
            .isTeam1Serving
            .onEach { isTeam1Serving ->
                _state.update { it.copy(isTeam1Serving = isTeam1Serving) }
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)

        matchTracker
            .goldenPoint
            .onEach { goldenPoint ->
                _state.update { it.copy(goldenPoint = goldenPoint) }
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)

        matchTracker
            .isMatchStarted
            .onEach { isMatchStarted ->
                _state.update { it.copy(isMatchStarted = isMatchStarted) }
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)

        matchTracker
            .elapsedTime
            .onEach { elapsedTime ->
                _state.update { it.copy(elapsedTime = elapsedTime) }
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)

        matchTracker
            .activeMatch
            .onEach { activeMatch ->
                val currentSets = activeMatch.getSetsForMatch()
                val currentGames = activeMatch.setList.last().getGamesForSet()
                val currentGame = activeMatch.setList.last().gameList.last()
                _state.update {
                    it.copy(
                        setsPlayer1 = currentSets.first,
                        gamesPlayer1 = currentGames.first,
                        pointsPlayer1 = currentGame.player1Points,
                        setsPlayer2 = currentSets.second,
                        gamesPlayer2 = currentGames.second,
                        pointsPlayer2 = currentGame.player2Points,
                    )
                }
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)

        matchTracker
            .currentHeartRate
            .onEach { heartRate ->
                _state.update { it.copy(currentHeartRate = heartRate) }
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)


        listenToWatchActions()
    }

    override fun reduce(intent: ActiveMatchIntent) {
        sendActionToWatch(intent)
        when (intent) {
            ActiveMatchIntent.AddPointToTeam2 -> matchTracker.addPointToPlayer2()
            ActiveMatchIntent.AddPointToTeam1 -> matchTracker.addPointToPlayer1()
            ActiveMatchIntent.UndoPoint -> matchTracker.undoPoint()
            ActiveMatchIntent.FinishMatch -> finishMatch()
            ActiveMatchIntent.DiscardMatch -> discardMatch()
            is ActiveMatchIntent.StartMatch -> startMatch(intent.isTeam1Serving)
            ActiveMatchIntent.CloseActiveDialog ->
                _state.update { it.copy(activeMatchDialogToShow = null) }

            is ActiveMatchIntent.ShowActiveDialog -> {
                _state.update { it.copy(activeMatchDialogToShow = intent.newActiveDialog) }
            }

            is ActiveMatchIntent.SubmitNotificationPermissionInfo -> {
                _state.update {
                    it.copy(
                        showNotificationRationale = intent.showNotificationPermissionRationale
                    )
                }
            }

            ActiveMatchIntent.DismissRationaleDialog ->
                _state.update { it.copy(showNotificationRationale = false) }


        }
    }

    private fun sendActionToWatch(intent: ActiveMatchIntent) {
        viewModelScope.launch {
            val messagingAction = when (intent) {
                ActiveMatchIntent.DiscardMatch -> MessagingAction.Discard
                ActiveMatchIntent.FinishMatch -> MessagingAction.Finish
                is ActiveMatchIntent.StartMatch -> MessagingAction.Start(intent.isTeam1Serving)
                ActiveMatchIntent.CloseActiveDialog -> MessagingAction.CloseError
                else -> null
            }
            messagingAction?.let {
                val result = watchConnector.sendActionToWatch(it)
                if (result is Result.Error) {
                    Timber.w("Tracker error: ${result.error}")
                }
            }
        }
    }

    private fun listenToWatchActions() {
        watchConnector
            .messagingActions
            .onEach { action ->
                when (action) {
                    ConnectionRequest -> {
                        if (state.value.isMatchStarted) {
                            watchConnector.sendActionToWatch(MessagingAction.Start(state.value.isTeam1Serving!!))
                        }
                    }

                    MessagingAction.Discard -> discardMatch()
                    MessagingAction.Finish -> finishMatch()
                    MessagingAction.CloseError -> {
                        _state.update {
                            it.copy(
                                error = null,
                                activeMatchDialogToShow = null
                            )
                        }
                    }

                    MessagingAction.RequestPointUpdate -> {
                        val currentState = state.value
                        val points = Pair(
                            currentState.pointsPlayer1.ordinal,
                            currentState.pointsPlayer2.ordinal
                        )
                        val games = Pair(currentState.gamesPlayer1, currentState.gamesPlayer2)
                        val sets = Pair(currentState.setsPlayer1, currentState.setsPlayer2)
                        watchConnector.sendActionToWatch(
                            MessagingAction.TotalUpdate(
                                points = points,
                                games = games,
                                sets = sets
                            )
                        )
                    }

                    else -> Unit
                }
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)
    }

    private fun startMatch(isTeam1Serving: Boolean) {
        matchTracker.setIsTeam1Serving(isTeam1Serving)
        matchTracker.setIsMatchStarted(true)
    }

    private fun finishMatch() {
        _state.update { it.copy(insertMatchLoading = true, loading = true) }
        ioLaunch {
            when (val insert = matchTracker.finishMatch()) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            insertMatchLoading = false,
                            activeMatchDialogToShow = null,
                            isMatchFinished = true
                        )
                    }
                    sendEvent(ActiveMatchEvent.NavToHistoryScreen())
                }

                is Result.Error -> {
                    Timber.e("[Viewmodel] Error occurred when finishing match -> ${insert.error}: ${insert.error.asUiText()}")

                    if (insert.error is DataError.Network) {
                        discardMatch(toastMessage = R.string.error_uploading_match_to_strava)
                        val result = watchConnector.sendActionToWatch(MessagingAction.Discard)
                        if (result is Result.Error)
                            Timber.w("[Viewmodel] Watch connector error -> ${result.error}: ${result.error.asUiText()}")
                    }

                    _state.update {
                        it.copy(
                            insertMatchLoading = false,
                            loading = false,
                            activeMatchDialogToShow = ActiveMatchDialog.ERROR,
                            error = insert.error.asUiText(),
                        )
                    }

                    val result = watchConnector.sendActionToWatch(MessagingAction.FinishMatchError)
                    if (result is Result.Error)
                        Timber.w("[Viewmodel] Watch connector error -> ${result.error}: ${result.error.asUiText()}")

                }
            }

        }
    }

    private fun discardMatch(toastMessage: Int? = null) {
        if (state.value.isMatchFinished) return
        _state.update {
            it.copy(
                activeMatchDialogToShow = null,
                isMatchFinished = true,
                loading = true
            )
        }
        ioLaunch {
            matchTracker.discardMatch()
            sendEvent(ActiveMatchEvent.NavToHistoryScreen(toastMessage))
        }
    }

}
