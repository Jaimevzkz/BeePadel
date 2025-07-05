package com.vzkz.core.database.domain.model

import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration

data class MatchEntityModel(
    val matchId: UUID,
    val dateTimeUtc: ZonedDateTime,
    val elapsedTime: Duration
)
