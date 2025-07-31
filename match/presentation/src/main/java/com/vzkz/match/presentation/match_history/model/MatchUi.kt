package com.vzkz.match.presentation.match_history.model

import java.util.UUID

data class MatchUi(
    val matchId: UUID,
    val isMatchWon: Boolean,
    val formatedSetList: List<Pair<Int,Int>>,
    val dateTimeFormatted: String,
    val elapsedTime: String
)