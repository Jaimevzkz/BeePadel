package com.vzkz.common.general.fake

import com.vzkz.core.domain.SessionStorage
import com.vzkz.core.domain.auth.AuthInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

class FakeSessionStorage : SessionStorage {
    override val tokensAvailable: StateFlow<Boolean>
        get() =
            MutableStateFlow<Boolean>(false)

    override suspend fun get(): AuthInfo? {
        return null
    }

    override suspend fun set(info: AuthInfo?) {

    }
}