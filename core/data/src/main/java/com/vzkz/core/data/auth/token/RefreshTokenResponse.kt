package com.vzkz.core.data.auth.token

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@InternalSerializationApi
data class RefreshTokenResponse(
    val expires_at: Int,
    val expires_in: Int,
    val refresh_token: String,
    val access_token: String
)