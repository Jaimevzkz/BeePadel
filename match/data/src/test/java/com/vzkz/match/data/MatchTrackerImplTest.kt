package com.vzkz.match.data

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import com.vzkz.common.general.TestDispatchers
import com.vzkz.common.general.fake.FakeLocalStorageRepository
import com.vzkz.common.test.util.MainCoroutineExtension
import com.vzkz.match.domain.model.Game
import com.vzkz.match.domain.model.Match
import com.vzkz.match.domain.model.Points
import com.vzkz.match.domain.model.Set
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.math.PI
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
            expectedGame = Game(randomUUID, Points.Forty, Points.Advantage)
            val fourthEmission = awaitItem()
            assertThat(fourthEmission.setList.first().gameList.first()).isEqualTo(expectedGame)
        }
    }

    @Test
    fun `Finishing a game`() = runTest {
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
            matchTrackerImpl.addPointToPlayer2()
            awaitItem()
            matchTrackerImpl.addPointToPlayer2() // 40-Won
            val firstEmission = awaitItem()
            val expectedGame = Game(randomUUID, Points.Zero, Points.Zero)
            val expectedPreviousGame = Game(randomUUID, Points.Forty, Points.Won)
            assertThat(firstEmission.setList.first().gameList[1]).isEqualTo(expectedGame)
            assertThat(firstEmission.setList.first().gameList.first()).isEqualTo(expectedPreviousGame)
        }
    }
}