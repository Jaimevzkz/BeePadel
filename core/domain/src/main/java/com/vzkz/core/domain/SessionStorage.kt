package com.vzkz.core.domain

import com.vzkz.core.domain.auth.AuthInfo
import kotlinx.coroutines.flow.StateFlow

interface SessionStorage {
    val tokensAvailable: StateFlow<Boolean>

    suspend fun get(): AuthInfo?

    suspend fun set(info: AuthInfo?)
}