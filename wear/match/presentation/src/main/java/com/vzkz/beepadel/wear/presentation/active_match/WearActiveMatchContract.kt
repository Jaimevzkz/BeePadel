package com.vzkz.beepadel.wear.presentation.active_match

import com.vzkz.beepadel.wear.presentation.active_match.model.WearDialogs
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
    val dialogToShow: WearDialogs,
    val heartRate: Int,
    //
    val hasMatchStarted: Boolean,
    val isConnectedPhoneNearBy: Boolean,
    val canTrackHeartRate: Boolean,
    val isAmbientMode: Boolean,
    val burnInProtectionRequired: Boolean
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
            hasMatchStarted = false,
            isConnectedPhoneNearBy = false,
            canTrackHeartRate = false,
            isAmbientMode = false,
            burnInProtectionRequired = false,
            dialogToShow = WearDialogs.NONE,
            heartRate = 0,
//            dialogToShow = WearDialogs.PHONE_NOT_CONNECTED,
        )
    }
}

sealed class WearActiveMatchIntent : Intent {
    data object FinishMatch : WearActiveMatchIntent()
    data object DiscardMatch : WearActiveMatchIntent()
    data object AddPointToTeam1 : WearActiveMatchIntent()
    data object AddPointToTeam2 : WearActiveMatchIntent()
    data object UndoPoint : WearActiveMatchIntent()
    data class ToggleDialog(val newVal: WearDialogs) : WearActiveMatchIntent()
    data class OnBodySensorPermissionResult(val isGranted: Boolean) : WearActiveMatchIntent()

}

sealed class WearActiveMatchEvent : Event {

}
