@file:OptIn(InternalSerializationApi::class)

package com.vzkz.core.data.auth.token

import io.ktor.http.Parameters
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val client_id: Int,
    val client_secret: String,
    val code: String,
    val grant_type: String,
)

fun RefreshTokenRequest.toParameters() = Parameters.build {
    append("client_id", client_id.toString())
    append("client_secret", client_secret)
    append("code", code)
    append("grant_type", grant_type)
}
