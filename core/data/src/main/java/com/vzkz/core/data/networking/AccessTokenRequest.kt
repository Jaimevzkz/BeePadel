package com.vzkz.core.data.networking

import kotlinx.serialization.Serializable

@Serializable
@kotlinx.serialization.InternalSerializationApi
data class AccessTokenRequest(
    val refreshToken: String,
    val userId: String
)