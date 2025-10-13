package com.vzkz.core.domain.auth

data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
)
