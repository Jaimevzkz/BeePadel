@file:OptIn(InternalSerializationApi::class)

package com.vzkz.match.data.networking

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class CreateStravaActivityRequest(
    val name: String,
)
