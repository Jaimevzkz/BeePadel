package com.vzkz.match.presentation.active_match

import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State
import com.vzkz.match.domain.model.Points
import kotlin.time.Duration

data class ActiveMatchState(
    val setsPlayer1: Int,
    val gamesPlayer1: Int,
    val pointsPlayer1: Points,
    val setsPlayer2: Int,
    val gamesPlayer2: Int,
    val pointsPlayer2: Points,
    val isServing: Boolean,
    val elapsedTime: Duration,
    ) : State {
    companion object {
        val initial: ActiveMatchState = ActiveMatchState(
            setsPlayer1 = 0,
            gamesPlayer1 = 0,
            pointsPlayer1 = Points.Zero,
            setsPlayer2 = 0,
            gamesPlayer2 = 0,
            pointsPlayer2 = Points.Zero,
            isServing = true,
            elapsedTime = Duration.ZERO,
        )
    }
}

sealed class ActiveMatchIntent : Intent {
    data object FinishMatch : ActiveMatchIntent()
    data object DiscardMatch : ActiveMatchIntent()
    data object AddPointToPlayer1 : ActiveMatchIntent()
    data object AddPointToPlayer2 : ActiveMatchIntent()
    data object UndoPoint : ActiveMatchIntent()
}

sealed class ActiveMatchEvent : Event {
    data object NavToHistoryScreen : ActiveMatchEvent()
}
