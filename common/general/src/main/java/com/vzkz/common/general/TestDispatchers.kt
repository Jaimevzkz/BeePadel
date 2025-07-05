package com.vzkz.common.general

import com.vzkz.core.domain.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher

class TestDispatchers(
    private val testDispatcher: CoroutineDispatcher
): DispatchersProvider {
    override val main: CoroutineDispatcher
        get() = testDispatcher
    override val mainImmediate: CoroutineDispatcher
        get() = testDispatcher
    override val io: CoroutineDispatcher
        get() = testDispatcher
    override val default: CoroutineDispatcher
        get() = testDispatcher

}