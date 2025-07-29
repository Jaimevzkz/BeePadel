package com.vzkz.match.data.match_history

import com.vzkz.common.general.TestDispatchers
import com.vzkz.common.general.fake.FakeLocalStorageRepository
import com.vzkz.common.test.util.MainCoroutineExtension
import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.match.data.match_historrepoimy.MatchHistoryRepositoryImpl
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.RegisterExtension

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

        matchHistoryRepository =
            MatchHistoryRepositoryImpl(localStorageRepository = fakeLocalStorageRepository)

    }

}