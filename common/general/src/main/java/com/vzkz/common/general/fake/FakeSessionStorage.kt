package com.vzkz.common.general.fake

import com.vzkz.core.domain.SessionStorage
import com.vzkz.core.domain.auth.AuthInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeSessionStorage : SessionStorage {
    private val tokensAvailableFlow = MutableStateFlow(false)
    private var authInfo: AuthInfo? = null

    override val tokensAvailable: StateFlow<Boolean>
        get() = tokensAvailableFlow


    override suspend fun get(): AuthInfo? {
        return authInfo
    }

    override suspend fun set(info: AuthInfo?) {
        authInfo = info
    }
}