package com.vzkz.beepadel.wear.presentation.active_match

import com.vzkz.core.presentation.ui.UiText
import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State
import com.vzkz.match.domain.model.Points
import com.vzkz.match.domain.model.Points.Zero
import kotlin.time.Duration

data class WearActiveMatchState(
    val error: UiText?,
    val setsPlayer1: Int,
    val gamesPlayer1: Int,
    val pointsPlayer1: Points,
    val setsPlayer2: Int,
    val gamesPlayer2: Int,
    val pointsPlayer2: Points,
    val isTeam1Serving: Boolean?,
    val elapsedTime: Duration,
    val isMatchResumed: Boolean,
    val isMatchStarted: Boolean,
    val isMatchFinished: Boolean,
    val showServingDialog: Boolean
) : State {
    companion object {
        val initial: WearActiveMatchState = WearActiveMatchState(
            error = null,
            setsPlayer1 = 0,
            gamesPlayer1 = 0,
            pointsPlayer1 = Zero,
            setsPlayer2 = 0,
            gamesPlayer2 = 0,
            pointsPlayer2 = Zero,
            isTeam1Serving = null,
            elapsedTime = Duration.ZERO,
            isMatchResumed = false,
            isMatchStarted = false,
            isMatchFinished = false,
            showServingDialog = true,
        )
    }
}

sealed class WearActiveMatchIntent : Intent {

}

sealed class WearActiveMatchEvent : Event {

}
