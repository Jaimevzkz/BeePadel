package com.vzkz.match.data.match_history

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.vzkz.common.general.TestDispatchers
import com.vzkz.common.general.data_generator.generateSet
import com.vzkz.common.general.data_generator.match
import com.vzkz.common.general.data_generator.secondMatch
import com.vzkz.common.general.fake.FakeLocalStorageRepository
import com.vzkz.common.test.util.MainCoroutineExtension
import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.core.domain.error.Result
import com.vzkz.match.data.MatchTrackerImpl
import com.vzkz.match.data.match_historrepoimy.MatchHistoryRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class MatchHistoryRepositoryImplTest {

    private lateinit var testDispatchers: TestDispatchers

    // SUT
    private lateinit var matchHistoryRepository: MatchHistoryRepositoryImpl

    private lateinit var fakeLocalStorageRepository: LocalStorageRepository

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    @BeforeEach
    fun setUp() {
        testDispatchers = TestDispatchers(mainCoroutineExtension.testDispatcher)

        fakeLocalStorageRepository = FakeLocalStorageRepository()

        matchHistoryRepository = MatchHistoryRepositoryImpl(localStorageRepository = fakeLocalStorageRepository)

    }

}