package com.vzkz.match.data.networking

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@InternalSerializationApi
data class CreateStravaActivityResponse(
    @SerialName("id") val id: Long
)