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
import com.vzkz.match.data.active_match.ActiveMatchRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class MatchHistoryRepositoryImplTest {

    private lateinit var testDispatchers: TestDispatchers

    // SUT
    private lateinit var matchRepository: MatchHistoryRepositoryImpl

    private lateinit var activeMatchRepository: ActiveMatchRepositoryImpl
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

        matchRepository = MatchHistoryRepositoryImpl(localStorageRepository = fakeLocalStorageRepository)

        activeMatchRepository = ActiveMatchRepositoryImpl(localStorageRepository = fakeLocalStorageRepository)
    }

    @Test
    fun `test retrieving the match list`() = runTest {
        // Arrange
        val match = match().copy(
            setList = listOf(
                generateSet(6, 4)
            )
        )
        val secondMatch = secondMatch().copy(
            setList = listOf(generateSet(1, 6))
        )

        val expectedMatchList = mutableListOf(match)

        activeMatchRepository.insertMatch(match)

        // Act
        val matchListFlow = matchRepository.getMatchHistory()

        // Assert
        matchListFlow.test {
            // First emission
            val firstMatchListEmission = awaitItem()
            assertThat(firstMatchListEmission).isEqualTo(expectedMatchList)

            val insert = activeMatchRepository.insertMatch(secondMatch)
            assertThat(insert is Result.Success).isTrue()
            expectedMatchList.add(secondMatch)

            // Second emission
            val secondMatchListEmission = awaitItem()
            assertThat(secondMatchListEmission).isEqualTo(expectedMatchList)

            cancelAndIgnoreRemainingEvents()
        }

    }

}