package com.vzkz.connectivity.core.data.messaging

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
sealed interface MessagingActionDto {
    @Serializable
    data object StartOrResume : MessagingActionDto

    @Serializable
    data object Finish : MessagingActionDto

    @Serializable
    data object ConnectionRequest : MessagingActionDto

    @InternalSerializationApi @Serializable
    data class HeartRateUpdater(val heartRate: Int) : MessagingActionDto

    @InternalSerializationApi @Serializable
    data class PointsUpdate(val points: Pair<Int, Int>) : MessagingActionDto

    @InternalSerializationApi @Serializable
    data class GamesUpdate(val games: Pair<Int, Int>) : MessagingActionDto

    @InternalSerializationApi @Serializable
    data class SetsUpdate(val sets: Pair<Int, Int>) : MessagingActionDto

    @InternalSerializationApi @Serializable
    data class ServingUpdate(val isTeam1Serving: Boolean?) : MessagingActionDto

    @InternalSerializationApi @Serializable
    data class TimeUpdate(val elapsedDuration: Duration) : MessagingActionDto
}