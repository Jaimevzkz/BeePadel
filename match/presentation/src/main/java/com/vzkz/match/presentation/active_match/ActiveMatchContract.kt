package com.vzkz.match.presentation.active_match

import com.vzkz.core.presentation.ui.UiText
import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State
import com.vzkz.match.domain.model.Points
import com.vzkz.match.presentation.model.ActiveMatchDialog
import kotlin.time.Duration

data class ActiveMatchState(
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
    val activeMatchDialogToShow: ActiveMatchDialog?,
    val insertMatchLoading: Boolean,
    val showNotificationRationale: Boolean,
    val showServingDialog: Boolean
    ) : State {
    companion object {
        val initial: ActiveMatchState = ActiveMatchState(
            error = null,
            setsPlayer1 = 0,
            gamesPlayer1 = 0,
            pointsPlayer1 = Points.Zero,
            setsPlayer2 = 0,
            gamesPlayer2 = 0,
            pointsPlayer2 = Points.Zero,
            isTeam1Serving = null,
            elapsedTime = Duration.ZERO,
            isMatchResumed = false,
            isMatchStarted = false,
            isMatchFinished = false,
            activeMatchDialogToShow = null,
            insertMatchLoading = false,
            showNotificationRationale = false,
            showServingDialog = true,
        )
    }
}

sealed class ActiveMatchIntent : Intent {
    data class StartMatch(val isTeam1Serving: Boolean) : ActiveMatchIntent()
    data object FinishMatch : ActiveMatchIntent()
    data object DiscardMatch : ActiveMatchIntent()
    data object AddPointToPlayer1 : ActiveMatchIntent()
    data object AddPointToPlayer2 : ActiveMatchIntent()
    data object UndoPoint : ActiveMatchIntent()
    data object NavToHistoryScreen: ActiveMatchIntent()
    data object CloseActiveDialog: ActiveMatchIntent()
    data class ShowActiveDialog(val newActiveDialog: ActiveMatchDialog): ActiveMatchIntent()
    data class SubmitNotificationPermissionInfo(
        val acceptedNotificationPermission: Boolean,
        val showNotificationPermissionRationale: Boolean
    ): ActiveMatchIntent()
    data object DismissRationaleDialog: ActiveMatchIntent()
}

sealed class ActiveMatchEvent : Event {
    data object NavToHistoryScreen : ActiveMatchEvent()
}
