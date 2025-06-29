package com.vzkz.match.domain

import java.time.ZonedDateTime
import kotlin.time.Duration

data class Match (
    val setList: List<Set>,
    val dateTimeUtc: ZonedDateTime,
    val elapsedTime: Duration
)