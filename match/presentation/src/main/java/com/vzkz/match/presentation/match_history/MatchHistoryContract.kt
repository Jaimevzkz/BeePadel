package com.vzkz.match.presentation.match_history

import com.vzkz.core.domain.error.RootError
import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State


data class MatchHistoryState(
    val error: RootError?
) : State {
    companion object {
        val initial: MatchHistoryState = MatchHistoryState(
            error = null
        )
    }
}

sealed class MatchHistoryIntent: Intent {

}

sealed class MatchHistoryEvent: Event {
}
