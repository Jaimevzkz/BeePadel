package com.vzkz.match.presentation.active_match

import androidx.lifecycle.viewModelScope
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.Result
import com.vzkz.core.presentation.ui.BaseViewModel
import com.vzkz.core.presentation.ui.asUiText
import com.vzkz.match.domain.MatchTracker
import com.vzkz.match.presentation.model.ActiveMatchDialog
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ActiveMatchViewmodel(
    private val dispatchers: DispatchersProvider,
    private val matchTracker: MatchTracker
) :
    BaseViewModel<ActiveMatchState, ActiveMatchIntent, ActiveMatchEvent>(
        ActiveMatchState.initial,
        dispatchers
    ) {

    init {
        matchTracker
            .isTeam1Serving
            .onEach { isTeam1Serving ->
                _state.update { it.copy(isTeam1Serving = isTeam1Serving) }
            }
            .flowOn(dispatchers.default)
            .launchIn(viewModelScope)

        matchTracker
            .isMatchResumed
            .onEach { isMatchPlaying ->
                _state.update { it.copy(isMatchResumed = isMatchPlaying) }
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
    }

    override fun reduce(intent: ActiveMatchIntent) {
        when (intent) {
            ActiveMatchIntent.FinishMatch -> finishMatch()
            ActiveMatchIntent.AddPointToPlayer2 -> matchTracker.addPointToPlayer2()
            ActiveMatchIntent.AddPointToPlayer1 -> matchTracker.addPointToPlayer1()
            ActiveMatchIntent.DiscardMatch -> discardMatch()
            ActiveMatchIntent.UndoPoint -> matchTracker.undoPoint()
            ActiveMatchIntent.NavToHistoryScreen -> sendEvent(ActiveMatchEvent.NavToHistoryScreen)
            is ActiveMatchIntent.StartMatch -> startMatch(intent.isTeam1Serving)
            ActiveMatchIntent.CloseActiveDialog -> _state.update { it.copy(activeMatchDialogToShow = null) }
            is ActiveMatchIntent.ShowActiveDialog -> _state.update { it.copy(activeMatchDialogToShow = intent.newActiveDialog) }
        }
    }

    private fun startMatch(isTeam1Serving: Boolean) {
        matchTracker.setIsTeam1Serving(isTeam1Serving)
        matchTracker.setIsMatchStarted(true)
        matchTracker.setPlayingMatch(true)
    }

    private fun finishMatch() {
        _state.update { it.copy(insertMatchLoading = true) }
        ioLaunch {
            when (val insert = matchTracker.finishMatch()) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            insertMatchLoading = false,
                            activeMatchDialogToShow = null
                        )
                    }
                    sendEvent(ActiveMatchEvent.NavToHistoryScreen)
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            insertMatchLoading = false,
                            activeMatchDialogToShow = ActiveMatchDialog.ERROR,
                            error = insert.error.asUiText()
                        )
                    }
                }
            }

        }
    }

    private fun discardMatch() {
        ioLaunch { matchTracker.discardMatch() }
        _state.update { it.copy(activeMatchDialogToShow = null) }
        sendEvent(ActiveMatchEvent.NavToHistoryScreen)
    }
}
