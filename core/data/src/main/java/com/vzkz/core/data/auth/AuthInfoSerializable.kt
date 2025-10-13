package com.vzkz.core.data.auth

import kotlinx.serialization.Serializable

@Serializable
@kotlinx.serialization.InternalSerializationApi
data class AuthInfoSerializable(
    val accessToken: String,
    val refreshToken: String,

)