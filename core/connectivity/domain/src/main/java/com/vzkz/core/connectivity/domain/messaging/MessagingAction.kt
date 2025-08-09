package com.vzkz.core.connectivity.domain.messaging

import kotlin.time.Duration

sealed interface MessagingAction {
    // Bidirectional
    data object ConnectionRequest : MessagingAction
    data class Start(val isTeam1Serving: Boolean) : MessagingAction
    data object Finish : MessagingAction
    data object Discard : MessagingAction

    // Watch -> Phone
    data class HeartRateUpdate(val heartRate: Int) : MessagingAction
    data class AddPointTo(val addToTeam1: Boolean) : MessagingAction
    data object UndoPoint : MessagingAction

    // Phone -> Watch
    data class PointsUpdate(val points: Pair<Int, Int>) : MessagingAction
    data class GamesUpdate(val games: Pair<Int, Int>) : MessagingAction
    data class SetsUpdate(val sets: Pair<Int, Int>) : MessagingAction
    data class UpdateAfterUndo(
        val points: Pair<Int, Int>,
        val games: Pair<Int, Int>,
        val sets: Pair<Int, Int>
    ) : MessagingAction
    data class ServingUpdate(val isTeam1Serving: Boolean?) : MessagingAction
    data class TimeUpdate(val elapsedDuration: Duration) : MessagingAction
}