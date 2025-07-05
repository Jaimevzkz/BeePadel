package com.vzkz.match.presentation.match_history

import androidx.lifecycle.viewModelScope
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.presentation.ui.BaseViewModel
import com.vzkz.match.domain.match_history.MatchHistoryRepository
import com.vzkz.match.presentation.util.toMatchUi
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.util.UUID

class MatchHistoryViewModel(
    private val dispatchers: DispatchersProvider,
    private val matchHistoryRepository: MatchHistoryRepository
) :
    BaseViewModel<MatchHistoryState, MatchHistoryIntent, MatchHistoryEvent>(
        MatchHistoryState.initial,
        dispatchers
    ) {

    override fun reduce(intent: MatchHistoryIntent) {
        when (intent) {
            is MatchHistoryIntent.NavigateToActiveMatch -> sendEvent(MatchHistoryEvent.NavigateToActiveMatch)
            is MatchHistoryIntent.DeleteMatch -> deleteMatch(intent.matchId)
        }
    }

    init {
        matchHistoryRepository.getMatchHistory()
            .onEach { matchList ->
                val matchUiList = matchList.map { it.toMatchUi() }
                _state.update {
                    it.copy(matchHistory = matchUiList)
                }
            }
            .flowOn(dispatchers.io)
            .launchIn(viewModelScope)
    }

    private fun deleteMatch(matchId: UUID) {
        ioLaunch {
            matchHistoryRepository.deleteMatch(matchId)
        }
    }
}
