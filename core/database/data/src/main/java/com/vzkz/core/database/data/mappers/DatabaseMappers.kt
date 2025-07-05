package com.vzkz.core.database.data.mappers

import com.vzkz.core.database.domain.model.GameEntityModel
import com.vzkz.core.database.domain.model.MatchEntityModel
import com.vzkz.core.database.domain.model.SetEntityModel
import com.vzkz.match.domain.model.Game
import com.vzkz.match.domain.model.Match
import com.vzkz.match.domain.model.Set
import game.GameEntity
import match.MatchEntity
import set.SetEntity

fun GameEntity.toDbDomain(): GameEntityModel {
    return GameEntityModel(
        gameId = gameId,
        serverPoints = serverPoints,
        receiverPoints = receiverPoints,
        setId = setId
    )
}

fun SetEntity.toDbDomain(): SetEntityModel {
    return SetEntityModel(
        setId = setId,
        matchId = matchId
    )
}

fun MatchEntity.toDbDomain(): MatchEntityModel {
    return MatchEntityModel(
        matchId = matchId,
        dateTimeUtc = dateTimeUtc,
        elapsedTime = elapsedTime
    )
}
