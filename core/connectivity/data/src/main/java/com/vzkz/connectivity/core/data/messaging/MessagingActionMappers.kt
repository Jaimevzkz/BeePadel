@file:OptIn(InternalSerializationApi::class)

package com.vzkz.connectivity.core.data.messaging

import com.vzkz.core.connectivity.domain.messaging.MessagingAction
import kotlinx.serialization.InternalSerializationApi

fun MessagingAction.toMessagingActionDto(): MessagingActionDto {
    return when (this) {
        is MessagingAction.AddPointTo -> MessagingActionDto.AddPointTo(team1 = addToTeam1)
        MessagingAction.ConnectionRequest -> MessagingActionDto.ConnectionRequest
        MessagingAction.Discard -> MessagingActionDto.Discard
        MessagingAction.Finish -> MessagingActionDto.Finish
        is MessagingAction.GamesUpdate -> MessagingActionDto.GamesUpdate(games = games)
        is MessagingAction.HeartRateUpdate -> MessagingActionDto.HeartRateUpdate(heartRate = heartRate)
        is MessagingAction.PointsUpdate -> MessagingActionDto.PointsUpdate(points = points)
        is MessagingAction.ServingUpdate -> MessagingActionDto.ServingUpdate(isTeam1Serving = isTeam1Serving)
        is MessagingAction.SetsUpdate -> MessagingActionDto.SetsUpdate(sets = sets)
        is MessagingAction.Start -> MessagingActionDto.Start(isTeam1Serving = isTeam1Serving)
        is MessagingAction.TimeUpdate -> MessagingActionDto.TimeUpdate(elapsedDuration = elapsedDuration)
        MessagingAction.UndoPoint -> MessagingActionDto.UndoPoint
        is MessagingAction.UpdateAfterUndo -> MessagingActionDto.UpdateAfterUndo(
            points = points,
            games = games,
            sets = sets
        )
    }
}

fun MessagingActionDto.toMessagingAction(): MessagingAction {
    return when (this) {
        is MessagingActionDto.AddPointTo -> MessagingAction.AddPointTo(addToTeam1 = team1)
        MessagingActionDto.ConnectionRequest -> MessagingAction.ConnectionRequest
        MessagingActionDto.Discard -> MessagingAction.Discard
        MessagingActionDto.Finish -> MessagingAction.Finish
        is MessagingActionDto.GamesUpdate -> MessagingAction.GamesUpdate(games = games)
        is MessagingActionDto.HeartRateUpdate -> MessagingAction.HeartRateUpdate(heartRate = heartRate)
        is MessagingActionDto.PointsUpdate -> MessagingAction.PointsUpdate(points = points)
        is MessagingActionDto.ServingUpdate -> MessagingAction.ServingUpdate(isTeam1Serving = isTeam1Serving)
        is MessagingActionDto.SetsUpdate -> MessagingAction.SetsUpdate(sets = sets)
        is MessagingActionDto.Start -> MessagingAction.Start(isTeam1Serving = isTeam1Serving)
        is MessagingActionDto.TimeUpdate -> MessagingAction.TimeUpdate(elapsedDuration = elapsedDuration)
        MessagingActionDto.UndoPoint -> MessagingAction.UndoPoint
        is MessagingActionDto.UpdateAfterUndo -> MessagingAction.UpdateAfterUndo(
            points = points,
            games = games,
            sets = sets
        )
    }
}