package com.vzkz.core.domain.auth

interface AuthRepository {
    suspend fun isLoggedIn(): Boolean

    suspend fun <T> withFreshAccessToken(action: suspend (accessToken: String) -> T): T
}