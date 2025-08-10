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
) : BaseViewModel<MatchHistoryState, MatchHistoryIntent, MatchHistoryEvent>(
    MatchHistoryState.initial,
    dispatchers
) {

    private var uuidOfMatchToDelete: UUID? = null

    override fun reduce(intent: MatchHistoryIntent) {
        when (intent) {
            is MatchHistoryIntent.NavigateToActiveMatch -> sendEvent(MatchHistoryEvent.NavigateToActiveMatch)
            MatchHistoryIntent.NavigateToSettings -> sendEvent(MatchHistoryEvent.NavigateToSettings)
            is MatchHistoryIntent.DeleteMatch -> deleteMatch()
            is MatchHistoryIntent.ToggleDeleteDialog -> {
                uuidOfMatchToDelete = intent.matchId
                _state.update { it.copy(showDeleteDialog = intent.showDialog) }
            }
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

    private fun deleteMatch() {
        uuidOfMatchToDelete?.let { uuid ->
            ioLaunch {
                matchHistoryRepository.deleteMatch(uuid)
                _state.update { it.copy(showDeleteDialog = false) }
                uuidOfMatchToDelete = null
            }
        }
    }
}
