package com.vzkz.core.database.data.util

import com.vzkz.core.domain.model.Game
import com.vzkz.core.domain.model.Match
import com.vzkz.core.domain.model.Set
import game.GameEntity
import match.MatchEntity
import set.SetEntity

fun GameEntity.toDomain(): Game {
    return Game(
        gameId = gameId,
        player1Points = serverPoints,
        player2Points = receiverPoints
    )
}

fun SetEntity.toDomain(gameList: List<Game>): Set {
    return Set(
        setId = setId,
        gameList = gameList
    )
}

fun MatchEntity.toDomain(setList: List<Set>): Match {
    return Match(
        setList = setList,
        dateTime = dateTimeUtc,
        elapsedTime = elapsedTime,
        matchId = matchId,
        avgHeartRate = avgHeartRate?.toInt(),
        maxHeartRate = maxHeartRate?.toInt()
    )
}
