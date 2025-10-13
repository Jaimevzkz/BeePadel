package com.vzkz.core.data.auth.token

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
@InternalSerializationApi
data class AccessTokenRequest(
    val clientId: Int,
    val clientSecret: String,
    val grantType: String,
    val refreshToken: String,
)