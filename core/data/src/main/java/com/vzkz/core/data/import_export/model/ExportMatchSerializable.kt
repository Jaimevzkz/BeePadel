package com.vzkz.core.data.import_export.model

import com.vzkz.core.domain.model.Match
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Serializable
data class ExportMatchSerializable(
    val matchId: String,
    val dateTimeIso: String,
    val elapsedSeconds: Long,
    val avgHeartRate: Int?,
    val maxHeartRate: Int?,
    val sets: List<ExportSetSerializable>
) {
    fun toMatch(): Match = Match(
        matchId = UUID.fromString(matchId),
        setList = sets.map { it.toSet() },
        dateTime = ZonedDateTime.parse(dateTimeIso),
        elapsedTime = elapsedSeconds.seconds,
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate
    )
}

fun Match.toExportMatchSerializable(): ExportMatchSerializable = ExportMatchSerializable(
    matchId = matchId.toString(),
    dateTimeIso = dateTime.toOffsetDateTime().toString(),
    elapsedSeconds = elapsedTime.inWholeSeconds,
    avgHeartRate = avgHeartRate,
    maxHeartRate = maxHeartRate,
    sets = setList.map { it.toExportSetSerializable() }
)