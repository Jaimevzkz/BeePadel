package com.vzkz.match.presentation.match_history

import com.vzkz.core.domain.error.RootError
import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State
import com.vzkz.match.domain.Match


data class MatchHistoryState(
    val matchHistory: List<Match>
) : State {
    companion object {
        val initial: MatchHistoryState = MatchHistoryState(
            matchHistory = emptyList()
        )
    }
}

sealed class MatchHistoryIntent : Intent {
    data class NavigateToActiveMatch(val matchId: Int) : MatchHistoryIntent()
}

sealed class MatchHistoryEvent : Event {
    data class NavigateToActiveMatch(val matchId: Int) : MatchHistoryEvent()
}
