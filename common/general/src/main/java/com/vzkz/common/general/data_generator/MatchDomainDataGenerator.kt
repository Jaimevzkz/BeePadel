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

fun matchList(): List<Match> {
    return List(10) { index ->
        if (index % 2 == 0)
            match().copy(
                setList = listOf(
                    generateSet(3, 6),
                    generateSet(6, 2),
                    generateSet(6, 7),
                )
            )
        else
            match()
    }
}

fun match(): Match {
    return Match(
        setList = listOf(
            generateSet(6, 4),
            generateSet(6, 2),
            generateSet(2, 6),
            generateSet(7, 5),
        ),
        dateTimeUtc = ZonedDateTime.of(
            2025, 6, 29,
            14, 30, 0, 0,
            ZoneId.systemDefault()
        ),
        elapsedTime = 1.hours + 30.minutes + 43.seconds,
        matchId = UUID.randomUUID()
    )
}

fun secondMatch(): Match {
    return Match(
        setList = listOf(
            generateSet(4, 6),
            generateSet(3, 6),
            generateSet(6, 4),
            generateSet(6, 7),
        ),
        dateTimeUtc = ZonedDateTime.of(
            2025, 6, 29,
            14, 30, 0, 0,
            ZoneId.of("UTC")
        ),
        elapsedTime = 1.hours + 30.minutes + 43.seconds,
        matchId = UUID.randomUUID()
    )
}

fun generateSet(games1: Int, games2: Int): Set {
    val gameList = mutableListOf<Game>()
    repeat((1..games1).count()) {
        gameList.add(game(Won, Zero))
    }
    repeat((1..games2).count()) {
        gameList.add(game(Fifteen, Won))
    }
    return Set(
        setId = UUID.randomUUID(),
        gameList = gameList
    )
}

fun set(): Set {
    return Set(
        setId = UUID.randomUUID(),
        gameList = listOf(
            game(Zero, Won),
            game(Won, Thirty),
            game(Won, Forty),
            game(Zero, Won),
            game(Won, Thirty),
            game(Zero, Won),
            game(Won, Fifteen),
            game(Fifteen, Won),
            game(Won, Thirty),
            game(Won, Forty),
        ),
    )
}

fun game(serverPoints: Points, receiverPoints: Points): Game {
    return Game(
        gameId = UUID.randomUUID(),
        player1Points = serverPoints,
        player2Points = receiverPoints,
    )
}

fun emptyGame(): Game {
    return Game(
        gameId = UUID.randomUUID(),
        player1Points = Zero,
        player2Points = Zero
    )
}

fun emptySet(): Set {
    return Set(
        setId = UUID.randomUUID(),
        gameList = listOf(emptyGame())
    )
}

fun emptyMatch(): Match {
    return Match(
        matchId = UUID.randomUUID(),
        setList = listOf(emptySet()),
        dateTimeUtc = ZonedDateTime.now(),
        elapsedTime = Duration.ZERO,
    )
}