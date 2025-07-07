package com.vzkz.match.presentation.active_match

import com.vzkz.common.general.TestDispatchers
import org.junit.jupiter.api.extension.RegisterExtension
import com.vzkz.common.test.util.MainCoroutineExtension
import org.junit.jupiter.api.BeforeEach

class ActiveMatchViewmodelTest {
    private lateinit var testDispatchers: TestDispatchers

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    @BeforeEach
    fun setUp(){
        testDispatchers = TestDispatchers(mainCoroutineExtension.testDispatcher)
    }



}