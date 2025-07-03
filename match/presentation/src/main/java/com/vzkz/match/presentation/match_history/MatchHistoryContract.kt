package com.vzkz.match.presentation.match_history

import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State
import com.vzkz.match.presentation.match_history.model.MatchUi
import java.util.UUID


data class MatchHistoryState(
    val matchHistory: List<MatchUi>
) : State {
    companion object {
        val initial: MatchHistoryState = MatchHistoryState(
            matchHistory = emptyList()
        )
    }
}

sealed class MatchHistoryIntent : Intent {
    data object NavigateToActiveMatch : MatchHistoryIntent()
    data class DeleteMatch(val matchId: UUID):MatchHistoryIntent()
}

sealed class MatchHistoryEvent : Event {
    data object NavigateToActiveMatch : MatchHistoryEvent()

}
