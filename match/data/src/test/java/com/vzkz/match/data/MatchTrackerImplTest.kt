package com.vzkz.match.data

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.vzkz.common.general.TestDispatchers
import com.vzkz.common.general.fake.FakeLocalStorageRepository
import com.vzkz.common.test.util.MainCoroutineExtension
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.data.util.addGame
import com.vzkz.match.data.util.addSet
import com.vzkz.match.domain.model.Game
import com.vzkz.match.domain.model.Match
import com.vzkz.match.domain.model.Points
import com.vzkz.match.domain.model.Set
import io.kotlintest.matchers.types.shouldNotBeInstanceOf
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.math.exp
import kotlin.time.Duration

@ExtendWith(MockKExtension::class)
class MatchTrackerImplTest {

    private lateinit var testDispatchers: TestDispatchers

    // SUT
    private lateinit var matchTrackerImpl: MatchTrackerImpl

    private val fakeApplicationScope = CoroutineScope(SupervisorJob())

    private lateinit var fakeLocalStorageRepository: FakeLocalStorageRepository

    private val randomUUID = UUID.randomUUID()
    private val zonedDateTime = ZonedDateTime.now()

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    @BeforeEach
    fun setUp() {
        mockkStatic(UUID::class)
        mockkStatic(ZonedDateTime::class)
        every { UUID.randomUUID() } returns randomUUID
        every { ZonedDateTime.now() } returns zonedDateTime

        testDispatchers = TestDispatchers(mainCoroutineExtension.testDispatcher)
        fakeLocalStorageRepository = FakeLocalStorageRepository()
        matchTrackerImpl = MatchTrackerImpl(
            applicationScope = fakeApplicationScope,
            dispatchers = testDispatchers,
            localStorageRepository = fakeLocalStorageRepository
        )
    }

    @Test
    fun `Adding points to a game`() = runTest {
        matchTrackerImpl.setIsTeam1Serving(true)
        matchTrackerImpl.activeMatch.test {
            val firstEmission = awaitItem()
            assertThat(firstEmission).isEqualTo(
                Match(
                    matchId = UUID.randomUUID(),
                    setList = listOf(
                        Set(
                            UUID.randomUUID(), listOf(
                                Game(
                                    gameId = UUID.randomUUID(),
                                    player1Points = Points.Zero,
                                    player2Points = Points.Zero
                                )
                            )
                        )
                    ),
                    dateTimeUtc = ZonedDateTime.now(),
                    elapsedTime = Duration.ZERO,
                )
            )
            matchTrackerImpl.addPointToPlayer1()
            awaitItem()
            matchTrackerImpl.addPointToPlayer1()
            awaitItem()
            matchTrackerImpl.addPointToPlayer1()
            var expectedGame = Game(randomUUID, Points.Forty, Points.Zero)
            val secondEmission = awaitItem()
            assertThat(secondEmission.setList.first().gameList.first()).isEqualTo(expectedGame)

            matchTrackerImpl.addPointToPlayer2()
            awaitItem()
            matchTrackerImpl.addPointToPlayer2()
            awaitItem()

            matchTrackerImpl.addPointToPlayer2()
            expectedGame = Game(randomUUID, Points.Forty, Points.Forty)
            val thirdEmission = awaitItem()
            assertThat(thirdEmission.setList.first().gameList.first()).isEqualTo(expectedGame)

            matchTrackerImpl.addPointToPlayer2()
            expectedGame = Game(randomUUID, Points.Forty, Points.Advantage)
            val fourthEmission = awaitItem()
            assertThat(fourthEmission.setList.first().gameList.first()).isEqualTo(expectedGame)
        }
    }

    @Test
    fun `Finishing a game`() = runTest {
        matchTrackerImpl.setIsTeam1Serving(true)
        matchTrackerImpl.activeMatch.test {
            awaitItem()

            matchTrackerImpl.addPointToPlayer1()
            awaitItem()
            matchTrackerImpl.addPointToPlayer1()
            awaitItem()
            matchTrackerImpl.addPointToPlayer1() //  40-0
            awaitItem()

            matchTrackerImpl.addPointToPlayer2()
            awaitItem()
            matchTrackerImpl.addPointToPlayer2()
            awaitItem()
            matchTrackerImpl.addPointToPlayer2() // 40-40
            awaitItem()
            matchTrackerImpl.addPointToPlayer2() // 40-Ad
            awaitItem()
            matchTrackerImpl.addPointToPlayer2() // 40-Won
            val emission = awaitItem()
            val expectedGame = Game(randomUUID, Points.Zero, Points.Zero)
            val expectedPreviousGame = Game(randomUUID, Points.Forty, Points.Won)
            assertThat(emission.setList.first().gameList[1]).isEqualTo(expectedGame)
            assertThat(emission.setList.first().gameList.first()).isEqualTo(
                expectedPreviousGame
            )
        }
    }

    @Test
    fun `Finishing a set`() = runTest {
        matchTrackerImpl.setIsTeam1Serving(true)
        matchTrackerImpl.activeMatch.test {
            var emission = awaitItem()

            val expectedFirstSetResult = (6 to 3)
            emission = addSet(
                matchTrackerImpl = matchTrackerImpl,
                onAwait = { awaitItem() },
                desiredScore = expectedFirstSetResult
            )

            assertThat(emission.setList.size).isEqualTo(2)
            assertThat(emission.setList.first().getGamesForSet()).isEqualTo(expectedFirstSetResult)
            assertThat(emission.setList[1].getGamesForSet()).isEqualTo(Pair(0, 0))

            matchTrackerImpl.addPointToPlayer1()
            awaitItem()
            matchTrackerImpl.addPointToPlayer1()
            awaitItem()
            matchTrackerImpl.addPointToPlayer1()
            awaitItem()
            matchTrackerImpl.addPointToPlayer1()
            emission = awaitItem()
            assertThat(emission.setList[1].getGamesForSet()).isEqualTo(Pair(1, 0))

        }
    }

    @Test
    fun `Playing 3 sets and an extra game is cleaned up correctly`() = runTest{
        val expectedSet1Result = (6 to 3)
        val expectedSet2Result = (5 to 7)
        val expectedSet3Result = (6 to 2)
        val expectedSet4Result = (0 to 1)
        matchTrackerImpl.setIsTeam1Serving(true)
        matchTrackerImpl.activeMatch.test {
            var emission = awaitItem()

            addSet(
                matchTrackerImpl = matchTrackerImpl,
                onAwait = { awaitItem() },
                desiredScore = expectedSet1Result
            )
            addSet(
                matchTrackerImpl = matchTrackerImpl,
                onAwait = { awaitItem() },
                desiredScore = expectedSet2Result
            )
            addSet(
                matchTrackerImpl = matchTrackerImpl,
                onAwait = { awaitItem() },
                desiredScore = expectedSet3Result
            )

            emission = addGame(
                matchTrackerImpl = matchTrackerImpl,
                team1 = false,
                onAwait = {awaitItem()}
            )

            val set1 = emission.setList[0].getGamesForSet()
            val set2 = emission.setList[1].getGamesForSet()
            val set3 = emission.setList[2].getGamesForSet()
            val set4 = emission.setList[3].getGamesForSet()

            assertThat(set1).isEqualTo(expectedSet1Result)
            assertThat(set2).isEqualTo(expectedSet2Result)
            assertThat(set3).isEqualTo(expectedSet3Result)
            assertThat(set4).isEqualTo(expectedSet4Result)

        }
        matchTrackerImpl.finishMatch()

        val resultingMatchHistoryList = fakeLocalStorageRepository.getMatchHistory().first()

        val set1AfterInsert = resultingMatchHistoryList[0].setList[0].getGamesForSet()
        val set2AfterInsert = resultingMatchHistoryList[0].setList[1].getGamesForSet()
        val set3AfterInsert = resultingMatchHistoryList[0].setList[2].getGamesForSet()

        assertThat(resultingMatchHistoryList[0].setList.size).isEqualTo(3)
        assertThat(set1AfterInsert).isEqualTo(expectedSet1Result)
        assertThat(set2AfterInsert).isEqualTo(expectedSet2Result)
        assertThat(set3AfterInsert).isEqualTo(expectedSet3Result)
    }

    @Test
    fun `Trying to end a match without completing a set returns error`() = runTest{
        val expectedError = DataError.Logic.EMPTY_SET_LIST

        matchTrackerImpl.setIsTeam1Serving(true)
        matchTrackerImpl.activeMatch.test {
            awaitItem()

            addGame(
                matchTrackerImpl = matchTrackerImpl,
                team1 = false,
                onAwait = {awaitItem()}
            )
        }
        val result = matchTrackerImpl.finishMatch()

        assert(result is Result.Error)
        assertThat((result as Result.Error).error).isEqualTo(expectedError)
    }
}