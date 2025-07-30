package com.vzkz.match.domain.model

import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration

data class Match(
    val matchId: UUID,
    val setList: List<Set>,
    val dateTime: ZonedDateTime,
    val elapsedTime: Duration
){

    fun getSetsForMatch(): Pair<Int, Int> {
        var setsPlayer1 = 0
        var setsPlayer2 = 0
        setList.forEach { set ->
            if (set.getWinner() == true) setsPlayer1++
            else if (set.getWinner() == false) setsPlayer2++
        }

        return Pair(setsPlayer1, setsPlayer2)
    }
}