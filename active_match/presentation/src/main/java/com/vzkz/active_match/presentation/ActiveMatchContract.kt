package com.vzkz.active_match.presentation

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
}

sealed class ActiveMatchEvent: Event {
}
