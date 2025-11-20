@file:OptIn(InternalSerializationApi::class)

package com.vzkz.match.data.networking

import com.vzkz.core.data.networking.SPORT
import com.vzkz.core.data.networking.TYPE
import com.vzkz.match.domain.model.Match
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.ZoneOffset

@Serializable
data class CreateStravaActivityRequest(
    @SerialName("name") val name: String,
    @SerialName("type") val type: String,
    @SerialName("sport_type") val sportType: String,
    @SerialName("start_date_local") val startDateLocal: String,
    @SerialName("elapsed_time") val elapsedTime: Int,
    @SerialName("description") val description: String,
)

internal fun Match.createRequestFromMatch(): CreateStravaActivityRequest {
    return CreateStravaActivityRequest(
        name = "Pádel Match", // todo where to put this string?
        type = SPORT,
        sportType = TYPE,
        startDateLocal = dateTime.withZoneSameInstant(ZoneOffset.UTC).toString(),
        elapsedTime = elapsedTime.inWholeSeconds.toInt(),
        description = getFormattedResultOfMatch()
    )
}