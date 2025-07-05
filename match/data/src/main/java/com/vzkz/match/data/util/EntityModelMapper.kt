package com.vzkz.match.data.util

import com.vzkz.core.database.domain.model.GameEntityModel
import com.vzkz.core.database.domain.model.MatchEntityModel
import com.vzkz.core.database.domain.model.SetEntityModel
import com.vzkz.match.domain.model.Game
import com.vzkz.match.domain.model.Match
import com.vzkz.match.domain.model.Set

fun GameEntityModel.toDomain(): Game {
    return Game(
        gameId = gameId,
        serverPoints = serverPoints,
        receiverPoints = receiverPoints
    )
}

fun SetEntityModel.toDomain(gameList: List<Game>): Set {
    return Set(
        setId = setId,
        gameList = gameList
    )
}

fun MatchEntityModel.toDomain(setList: List<Set>): Match {
    return Match(
        matchId = matchId,
        setList = setList,
        dateTimeUtc = dateTimeUtc,
        elapsedTime = elapsedTime
    )
}
