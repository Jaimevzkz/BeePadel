package com.vzkz.core.data.auth.token

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@InternalSerializationApi
data class AccessTokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_at")val expirationTimestamp: Long
)