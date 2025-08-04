@file:OptIn(InternalSerializationApi::class)

package com.vzkz.connectivity.core.data.messaging

import com.vzkz.core.connectivity.domain.messaging.MessagingAction
import com.vzkz.core.connectivity.domain.messaging.MessagingAction.HeartRateUpdate
import kotlinx.serialization.InternalSerializationApi

fun MessagingAction.toMessagingActionDto(): MessagingActionDto {
    return when (this) {
        MessagingAction.ConnectionRequest -> MessagingActionDto.ConnectionRequest
        MessagingAction.Finish -> MessagingActionDto.Finish
        is MessagingAction.HeartRateUpdate -> MessagingActionDto.HeartRateUpdater(heartRate)
        MessagingAction.StartOrResume -> MessagingActionDto.StartOrResume
        is MessagingAction.TimeUpdate -> MessagingActionDto.TimeUpdate(elapsedDuration)
        is MessagingAction.GamesUpdate -> MessagingActionDto.GamesUpdate(games)
        is MessagingAction.PointsUpdate -> MessagingActionDto.PointsUpdate(points)
        is MessagingAction.ServingUpdate -> MessagingActionDto.ServingUpdate(isTeam1Serving)
        is MessagingAction.SetsUpdate -> MessagingActionDto.SetsUpdate(sets)
    }
}

fun MessagingActionDto.toMessagingAction(): MessagingAction {
    return when (this) {
        MessagingActionDto.ConnectionRequest -> MessagingAction.ConnectionRequest
        MessagingActionDto.Finish -> MessagingAction.Finish
        is MessagingActionDto.HeartRateUpdater -> HeartRateUpdate(heartRate)
        MessagingActionDto.StartOrResume -> MessagingAction.StartOrResume
        is MessagingActionDto.TimeUpdate -> MessagingAction.TimeUpdate(elapsedDuration)
        is MessagingActionDto.GamesUpdate -> MessagingAction.GamesUpdate(games)
        is MessagingActionDto.PointsUpdate -> MessagingAction.PointsUpdate(points)
        is MessagingActionDto.ServingUpdate -> MessagingAction.ServingUpdate(isTeam1Serving)
        is MessagingActionDto.SetsUpdate -> MessagingAction.SetsUpdate(sets)
    }
}