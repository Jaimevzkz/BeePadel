package com.vzkz.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
@kotlinx.serialization.InternalSerializationApi
data class AccessTokenResponse(
    val accessToken: String,
    val expirationTimestamp: Long
)