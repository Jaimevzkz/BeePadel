package com.vzkz.common.general.data_generator

import com.vzkz.match.domain.model.Game
import com.vzkz.match.domain.model.Match
import com.vzkz.match.domain.model.Points
import com.vzkz.match.domain.model.Points.*
import com.vzkz.match.domain.model.Set
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

fun dummyMatchList(randomizeUUIDs: Boolean = false): List<Match> {
    return List(10) { index ->
        if (index % 2 == 0)
            dummyMatch(randomizeUUIDs).copy(
                setList = listOf(
                    generateDummySet(randomizeUUIDs, 3, 6),
                    generateDummySet(randomizeUUIDs, 6, 2),
                    generateDummySet(randomizeUUIDs, 6, 7),
                )
            )
        else
            dummyMatch(randomizeUUIDs)
    }
}

fun dummyMatch(randomizeUUIDs: Boolean = false): Match {
    return Match(
        setList = listOf(
            generateDummySet(randomizeUUIDs, 6, 4),
            generateDummySet(randomizeUUIDs, 6, 2),
            generateDummySet(randomizeUUIDs, 2, 6),
            generateDummySet(randomizeUUIDs, 7, 5),
        ),
        dateTime = fixedZonedDateTime(),
        elapsedTime = 1.hours + 30.minutes + 43.seconds,
        matchId = defaultUUID(randomizeUUIDs),
        avgHeartRate = 100,
        maxHeartRate = 100
    )
}

fun generateDummySet(randomizeUUIDs: Boolean, games1: Int, games2: Int): Set {
    val gameList = mutableListOf<Game>()
    repeat((1..games1).count()) {
        gameList.add(dummyGame(randomizeUUIDs, Won, Zero))
    }
    repeat((1..games2).count()) {
        gameList.add(dummyGame(randomizeUUIDs, Fifteen, Won))
    }
    return Set(
        setId = defaultUUID(randomizeUUIDs),
        gameList = gameList
    )
}

fun dummySet(randomizeUUIDs: Boolean = false): Set {
    return Set(
        setId = defaultUUID(),
        gameList = listOf(
            dummyGame(randomizeUUIDs, Zero, Won),
            dummyGame(randomizeUUIDs, Won, Thirty),
            dummyGame(randomizeUUIDs, Won, Forty),
            dummyGame(randomizeUUIDs, Zero, Won),
            dummyGame(randomizeUUIDs, Won, Thirty),
            dummyGame(randomizeUUIDs, Zero, Won),
            dummyGame(randomizeUUIDs, Won, Fifteen),
            dummyGame(randomizeUUIDs, Fifteen, Won),
            dummyGame(randomizeUUIDs, Won, Thirty),
            dummyGame(randomizeUUIDs, Won, Forty),
        ),
    )
}

fun dummyGame(randomizeUUIDs: Boolean = false, serverPoints: Points, receiverPoints: Points): Game {
    return Game(
        gameId = defaultUUID(randomizeUUIDs),
        player1Points = serverPoints,
        player2Points = receiverPoints,
    )
}

fun emptyGame(uuid: UUID): Game {
    return Game(
        gameId = uuid,
        player1Points = Zero,
        player2Points = Zero
    )
}

fun emptySet(setId: UUID, gameId: UUID): Set {
    return Set(
        setId = setId,
        gameList = listOf(emptyGame(gameId))
    )
}

fun emptyMatch(matchId: UUID, setId: UUID, gameId: UUID, zonedDateTime: ZonedDateTime): Match {
    return Match(
        matchId = matchId,
        setList = listOf(emptySet(setId, gameId)),
        dateTime = zonedDateTime,
        elapsedTime = Duration.ZERO,
        avgHeartRate = 0,
        maxHeartRate = 0,
    )
}

fun fixedZonedDateTime(): ZonedDateTime = ZonedDateTime.of(
    2025, 6, 29,
    14, 30, 0, 0,
    ZoneId.of("UTC")
)

fun defaultUUID(randomize: Boolean = false): UUID =
    if (randomize) UUID.randomUUID() else UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
