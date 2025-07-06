package com.vzkz.match.presentation.active_match

import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State
import com.vzkz.match.domain.model.Points

data class ActiveMatchState(
    val ownSets: Int,
    val ownGames: Int,
    val ownPoints: Points,
    val otherSets: Int,
    val otherGames: Int,
    val otherPoints: Points,
    val isServing: Boolean

    ) : State {
    companion object {
        val initial: ActiveMatchState = ActiveMatchState(
            ownSets = 0,
            ownGames = 0,
            ownPoints = Points.Zero,
            otherSets = 0,
            otherGames = 0,
            otherPoints = Points.Zero,
            isServing = true,
        )
    }
}

sealed class ActiveMatchIntent : Intent {
    data object FinishMatch : ActiveMatchIntent()
}

sealed class ActiveMatchEvent : Event {
    data object NavToHistoryScreen : ActiveMatchEvent()
}
