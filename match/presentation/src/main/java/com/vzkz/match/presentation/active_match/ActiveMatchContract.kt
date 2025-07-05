package com.vzkz.match.presentation.active_match

import com.vzkz.core.domain.error.RootError
import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State

data class ActiveMatchState(
    val error: RootError?
) : State {
    companion object {
        val initial: ActiveMatchState = ActiveMatchState(
            error = null
        )
    }
}

sealed class ActiveMatchIntent: Intent {
    data object FinishMatch: ActiveMatchIntent()
}

sealed class ActiveMatchEvent: Event {
    data object NavToHistoryScreen: ActiveMatchEvent()
}
