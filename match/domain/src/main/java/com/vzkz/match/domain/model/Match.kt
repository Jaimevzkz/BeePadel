package com.vzkz.match.domain.model

import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration

data class Match(
    val matchId: UUID,
    val setList: List<Set>,
    val dateTime: ZonedDateTime,
    val elapsedTime: Duration,
    val avgHeartRate: Int?,
    val maxHeartRate: Int?
) {
    fun getSetsForMatch(): Pair<Int, Int> {
        return setList.getSetCount()
    }

    // 1 won / 0 draw / -1 lost
    fun getWinner(): Int {
        val sets = getSetsForMatch()
        val setDifference = sets.first - sets.second
        return when {
            setDifference < 0 -> -1
            setDifference > 0 -> 1
            else -> 0
        }
    }

    fun getFormattedResultOfMatch(): String {
        var returnValue = ""
        setList.forEachIndexed { index, set ->
            if (index > 0) returnValue += " / "
            val games = set.getGamesForSet()
            returnValue += "Set ${index + 1}: ${games.first}-${games.second}"
        }
        returnValue += when (getWinner()) {
            1 -> " -> WON"
            -1 -> " -> LOST"
            else -> " -> DRAW"
        }
        return returnValue
    }

}