package com.vzkz.match.presentation.match_history.model

import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration

data class MatchUi(
    val matchId: UUID,
    val isMatchWon: Boolean,
    val formatedSetList: List<Pair<Int,Int>>,
    val dateTimeUtc: String,
    val elapsedTime: String
)