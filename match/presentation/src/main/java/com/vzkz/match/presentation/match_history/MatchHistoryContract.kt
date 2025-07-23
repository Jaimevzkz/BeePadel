package com.vzkz.match.presentation.match_history

import com.vzkz.common.general.data_generator.match
import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State
import com.vzkz.match.presentation.match_history.model.MatchUi
import com.vzkz.match.presentation.util.toMatchUi
import java.util.UUID


data class MatchHistoryState(
    val matchHistory: List<MatchUi>,
    val showDeleteDialog: Boolean
) : State {
    companion object {
        val initial: MatchHistoryState = MatchHistoryState(
            matchHistory = emptyList(),
            showDeleteDialog = false
        )
    }
}

sealed class MatchHistoryIntent : Intent {
    data object NavigateToActiveMatch : MatchHistoryIntent()
    data object DeleteMatch : MatchHistoryIntent()
    data class ToggleDeleteDialog(val showDialog: Boolean, val matchId: UUID? = null) : MatchHistoryIntent()
}

sealed class MatchHistoryEvent : Event {
    data object NavigateToActiveMatch : MatchHistoryEvent()

}
