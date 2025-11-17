package com.vzkz.match.data.networking

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@InternalSerializationApi
data class CreateStravaActivityResponse(
    val id: Int
)