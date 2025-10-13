package com.vzkz.core.data.auth.token

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@InternalSerializationApi
data class AccessTokenResponse(
    val accessToken: String,
    val expirationTimestamp: Long
)