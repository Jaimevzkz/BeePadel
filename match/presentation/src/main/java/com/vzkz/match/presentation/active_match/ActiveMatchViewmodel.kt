package com.vzkz.match.presentation.active_match

import androidx.lifecycle.viewModelScope
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.Result
import com.vzkz.core.presentation.ui.BaseViewModel
import com.vzkz.match.domain.MatchTracker
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
            .isMatchPlaying
            .onEach { isMatchPlaying ->
                _state.update { it.copy(isMatchPlaying = isMatchPlaying) }
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
            ActiveMatchIntent.DiscardMatch -> matchTracker.discardMatch()
            ActiveMatchIntent.UndoPoint -> matchTracker.undoPoint()
        }
    }

    private fun finishMatch() { //todo impl
        ioLaunch {
            when (matchTracker.finishMatch()) {
                is Result.Success -> sendEvent(ActiveMatchEvent.NavToHistoryScreen)
                is Result.Error -> {
                    //todo handle errors
                }
            }

        }

    }
}
