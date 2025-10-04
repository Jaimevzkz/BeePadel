package com.vzkz.core.domain

import com.vzkz.core.domain.auth.AuthInfo

interface SessionStorage {
    suspend fun get(): AuthInfo?

    suspend fun set(info: AuthInfo?)
}