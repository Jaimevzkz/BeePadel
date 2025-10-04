@file:OptIn(InternalSerializationApi::class)

package com.vzkz.core.data.auth

import com.vzkz.core.domain.auth.AuthInfo
import kotlinx.serialization.InternalSerializationApi

fun AuthInfo.toAuthInfoSerializable(): AuthInfoSerializable {
    return AuthInfoSerializable(
        authState = authState
    )
}

fun AuthInfoSerializable.toAuthInfo(): AuthInfo {
    return AuthInfo(
        authState = authState
    )
}