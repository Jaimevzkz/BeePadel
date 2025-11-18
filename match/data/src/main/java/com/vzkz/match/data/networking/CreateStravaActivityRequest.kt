@file:OptIn(InternalSerializationApi::class)

package com.vzkz.match.data.networking

import com.vzkz.core.data.networking.SPORT
import com.vzkz.match.domain.model.Match
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import java.time.ZoneOffset

@Serializable
data class CreateStravaActivityRequest(
    val name: String,
    val type: String,
    val sport_type: String,
    val start_date_local: String,
    val elapsed_time: Int,
    val description: String,
)

internal fun Match.createRequestFromMatch(): CreateStravaActivityRequest {
    return CreateStravaActivityRequest(
        name = "Pádel Match", // todo where to put this string?
        type = SPORT,
        sport_type = SPORT,
        start_date_local = dateTime.withZoneSameInstant(ZoneOffset.UTC).toString(),
        elapsed_time = elapsedTime.inWholeSeconds.toInt(),
        description = getFormattedResultOfMatch()
    )
}