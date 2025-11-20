@file:OptIn(InternalSerializationApi::class)

package com.vzkz.core.data.auth.token

import io.ktor.http.Parameters
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    @SerialName("client_id") val clientId: Int,
    @SerialName("client_secret")val clientSecret: String,
    @SerialName("code") val code: String,
    @SerialName("grant_type")val grantType: String,
)

