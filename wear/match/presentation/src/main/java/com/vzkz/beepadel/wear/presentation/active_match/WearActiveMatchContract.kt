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
    val setsTeam1: Int,
    val gamesTeam1: Int,
    val pointsTeam1: Points,
    val setsTeam2: Int,
    val gamesTeam2: Int,
    val pointsTeam2: Points,
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
            setsTeam1 = 0,
            gamesTeam1 = 0,
            pointsTeam1 = Zero,
            setsTeam2 = 0,
            gamesTeam2 = 0,
            pointsTeam2 = Zero,
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
