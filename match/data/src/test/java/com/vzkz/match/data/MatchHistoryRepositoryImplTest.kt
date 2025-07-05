package com.vzkz.match.data

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.vzkz.common.general.TestDispatchers
import com.vzkz.common.general.data_generator.generateSet
import com.vzkz.common.general.fake.database.FakeGameDataSource
import com.vzkz.common.general.fake.database.FakeMatchDataSource
import com.vzkz.common.general.fake.database.FakeSetDataSource
import com.vzkz.common.general.data_generator.match
import com.vzkz.common.general.data_generator.secondMatch
import com.vzkz.common.test.util.MainCoroutineExtension
import com.vzkz.core.database.domain.GameDataSource
import com.vzkz.core.database.domain.MatchDataSource
import com.vzkz.core.database.domain.SetDataSource
import com.vzkz.core.domain.error.Result
import com.vzkz.match.active_match.ActiveMatchRepositoryImpl
import com.vzkz.match.match_history.MatchHistoryRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
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

    private lateinit var matchDataSource: MatchDataSource
    private lateinit var setDataSource: SetDataSource
    private lateinit var gameDataSource: GameDataSource

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    @BeforeEach
    fun setUp() {
        testDispatchers = TestDispatchers(mainCoroutineExtension.testDispatcher)
        matchDataSource = FakeMatchDataSource()
        setDataSource = FakeSetDataSource()
        gameDataSource = FakeGameDataSource()

        matchRepository = MatchHistoryRepositoryImpl(
            matchDataSource = matchDataSource,
            setDataSource = setDataSource,
            gameDataSource = gameDataSource,
            dispatchers = testDispatchers
        )

        activeMatchRepository = ActiveMatchRepositoryImpl(
            matchDataSource = matchDataSource,
            setDataSource = setDataSource,
            gameDataSource = gameDataSource,
            dispatchers = testDispatchers
        )
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