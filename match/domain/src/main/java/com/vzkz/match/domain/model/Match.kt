package com.vzkz.match.domain.model

import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration
import kotlin.uuid.ExperimentalUuidApi

data class Match (
    val matchId: UUID,
    val setList: List<Set>,
    val dateTimeUtc: ZonedDateTime,
    val elapsedTime: Duration
)