package com.vzkz.match.data.match_history

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.vzkz.common.general.TestDispatchers
import com.vzkz.common.general.data_generator.dummyMatch
import com.vzkz.common.general.fake.FakeLocalStorageRepository
import com.vzkz.common.test.util.MainCoroutineExtension
import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.match.data.MatchHistoryRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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
        runBlocking {
            fakeLocalStorageRepository.insertMatchList(
                listOf(
                    dummyMatch(randomizeUUIDs = true),
                    dummyMatch(randomizeUUIDs = true),
                )
            )
        }

        matchHistoryRepository =
            MatchHistoryRepositoryImpl(localStorageRepository = fakeLocalStorageRepository)
    }

    @Test
    fun `Check matches are retrieved correctly`() = runTest {
        val matchHistory = matchHistoryRepository.getMatchHistory().first()

        assertThat(matchHistory.size).isEqualTo(2)
    }

    @Test
    fun `Check deleting a match from the database`() = runTest {
        val matchHistory = matchHistoryRepository.getMatchHistory().first()
        matchHistoryRepository.deleteMatch(matchHistory.first().matchId)
        assertThat(matchHistoryRepository.getMatchHistory().first().size).isEqualTo(1)
    }

}