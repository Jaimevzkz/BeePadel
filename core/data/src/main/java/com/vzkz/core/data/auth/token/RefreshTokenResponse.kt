package com.vzkz.core.data.auth.token

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@InternalSerializationApi
data class RefreshTokenResponse(
    @SerialName("expires_at")val expiresAt: Int,
    @SerialName("expires_in")val expiresIn: Int,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("access_token")  val accessToken: String
)