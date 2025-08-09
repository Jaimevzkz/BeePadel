package com.vzkz.connectivity.core.data.messaging

import android.annotation.SuppressLint
import com.vzkz.core.connectivity.domain.messaging.MessagingAction
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
sealed interface MessagingActionDto {
    @Serializable @InternalSerializationApi
    data class Start(val isTeam1Serving: Boolean) : MessagingActionDto
    @Serializable
    data object Finish : MessagingActionDto
    @Serializable
    data object Discard : MessagingActionDto
    @Serializable
    data object ConnectionRequest : MessagingActionDto

    @Serializable @InternalSerializationApi
    data class HeartRateUpdate(val heartRate: Int) : MessagingActionDto

    @Serializable @InternalSerializationApi
    data class PointsUpdate(val points: Pair<Int, Int>) : MessagingActionDto
    @Serializable @InternalSerializationApi
    data class AddPointTo(val team1: Boolean) : MessagingActionDto
    @Serializable
    data object UndoPoint : MessagingActionDto

    @Serializable @InternalSerializationApi
    data class GamesUpdate(val games: Pair<Int, Int>) : MessagingActionDto
    @Serializable @InternalSerializationApi
    data class SetsUpdate(val sets: Pair<Int, Int>) : MessagingActionDto

    @Serializable @InternalSerializationApi
    data class UpdateAfterUndo(
        val points: Pair<Int, Int>,
        val games: Pair<Int, Int>,
        val sets: Pair<Int, Int>
    ) : MessagingActionDto

    @Serializable @InternalSerializationApi
    data class ServingUpdate(val isTeam1Serving: Boolean?) : MessagingActionDto
    @Serializable @InternalSerializationApi
    data class TimeUpdate(val elapsedDuration: Duration) : MessagingActionDto

}