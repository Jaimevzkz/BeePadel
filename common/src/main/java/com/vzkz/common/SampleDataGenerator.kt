package com.vzkz.common

import com.vzkz.match.domain.Game
import com.vzkz.match.domain.Match
import com.vzkz.match.domain.Points
import com.vzkz.match.domain.Points.*
import com.vzkz.match.domain.Set
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds


fun match(): Match {
    return Match(
        setList = listOf(
            set(),
            generateSet(6, 2),
            generateSet(2, 6),
            generateSet(7, 5),
        ),
        elapsedTime = 1.hours + 30.minutes + 43.seconds,
        dateTimeUtc = ZonedDateTime.of(
            2025, 6, 29,
            14, 30, 0, 0,
            ZoneId.of("UTC")
        )
    )
}

fun generateSet(games1: Int, games2: Int): Set{
    val gameList = mutableListOf<Game>()
    repeat((1..games1).count()) {
        gameList.add(game(Won, Zero))
    }
    repeat((1..games2).count()) {
        gameList.add(game(Fifteen, Won))
    }
    return Set(gameList)
}

fun set(): Set {
    return Set(
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
        serverId = 0,
        serverPoints = serverPoints,
        receiverPoints = receiverPoints
    )
}