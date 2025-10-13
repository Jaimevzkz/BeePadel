@file:OptIn(InternalSerializationApi::class)

package com.vzkz.core.data.auth

import com.vzkz.core.domain.auth.AuthInfo
import kotlinx.serialization.InternalSerializationApi

fun AuthInfo.toAuthInfoSerializable(): AuthInfoSerializable {
    return AuthInfoSerializable(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}

fun AuthInfoSerializable.toAuthInfo(): AuthInfo {
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken
    )
}