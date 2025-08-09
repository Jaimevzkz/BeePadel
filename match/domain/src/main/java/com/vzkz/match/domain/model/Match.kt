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
){

    fun getSetsForMatch(): Pair<Int, Int> {
       return setList.getSetCount()
    }
}