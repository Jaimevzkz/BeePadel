package com.vzkz.core.data.auth

import kotlinx.serialization.Serializable

@Serializable
@kotlinx.serialization.InternalSerializationApi
data class AuthInfoSerializable(
    val authState: String
)