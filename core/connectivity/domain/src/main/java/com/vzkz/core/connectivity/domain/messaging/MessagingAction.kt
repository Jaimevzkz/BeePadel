package com.vzkz.core.connectivity.domain.messaging

import kotlin.time.Duration

sealed interface MessagingAction {
    data object StartOrResume : MessagingAction
    data object Finish : MessagingAction
    data object ConnectionRequest : MessagingAction
    data class HeartRateUpdate(val heartRate: Int) : MessagingAction
    data class PointsUpdate(val points: Pair<Int, Int>) : MessagingAction
    data class GamesUpdate(val games: Pair<Int, Int>) : MessagingAction
    data class SetsUpdate(val sets: Pair<Int, Int>) : MessagingAction
    data class ServingUpdate(val isTeam1Serving: Boolean?) : MessagingAction
    data class TimeUpdate(val elapsedDuration: Duration) : MessagingAction
}