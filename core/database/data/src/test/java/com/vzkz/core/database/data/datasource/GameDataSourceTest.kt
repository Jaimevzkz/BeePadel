package com.vzkz.core.database.data.datasource

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.vzkz.common.general.data_generator.defaultUUID
import com.vzkz.common.general.data_generator.fixedZonedDateTime
import com.vzkz.core.database.data.util.DatabaseTest
import com.vzkz.core.database.data.util.gameEntity
import com.vzkz.core.domain.error.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

@OptIn(ExperimentalCoroutinesApi::class)
class GameDataSourceTest : DatabaseTest() {

    // SUT
    private lateinit var gameDataSource: GameDataSource

    @BeforeEach
    fun individualSetUp() {
        gameDataSource = GameDataSource(
            db = beePadelDB,
            dispatchers = testDispatchers
        )
    }

    @Test
    fun `creating a game`() = runTest {
        val expectedGame = gameEntity()

        gameDataSource.insertOrReplaceGameForSet(
            gameId = expectedGame.gameId,
            setId = expectedGame.setId,
            serverPoints = expectedGame.serverPoints,
            receiverPoints = expectedGame.receiverPoints
        )
        runCurrent()

        val result = gameDataSource.getGameListForSet(
            setId = expectedGame.setId
        )
        assertThat(result).isEqualTo(listOf(expectedGame))
    }

    @Test
    fun `Inserting multiple games with same id just updates them`() = runTest {
        val game = gameEntity()
        val expectedGameListSize = 1

        (0..<3).forEach { _ ->
            gameDataSource.insertOrReplaceGameForSet(
                gameId = game.gameId,
                setId = game.setId,
                serverPoints = game.serverPoints,
                receiverPoints = game.receiverPoints
            )
        }

        val resultingListSize = gameDataSource.getGameListForSet(game.setId).size

        assertThat(resultingListSize).isEqualTo(expectedGameListSize)
    }

    @Test
    fun `Deleting multiple games with the same set id works`() = runTest {
        val game = gameEntity()
        var expectedGameListSize = 4

        (0..<expectedGameListSize).forEach { _ ->
            gameDataSource.insertOrReplaceGameForSet(
                gameId = defaultUUID(true),
                setId = game.setId,
                serverPoints = game.serverPoints,
                receiverPoints = game.receiverPoints
            )
        }
        var resultingSize = gameDataSource.getGameListForSet(game.setId).size

        assertThat(resultingSize).isEqualTo(expectedGameListSize)

        expectedGameListSize = 0
        gameDataSource.deleteGamesWithSetId(game.setId)
        resultingSize = gameDataSource.getGameListForSet(game.setId).size

        assertThat(resultingSize).isEqualTo(expectedGameListSize)
    }

    @Test
    fun `inserting multiple games and the getting them`() = runTest {
        val game = gameEntity()
        val listSize = 5
        val gameList = List(listSize) {
            game.copy(
                gameId = defaultUUID(true),
            )
        }

        val differentSetId = defaultUUID(randomize = true)
        val gameListNotToBeRetrieved = List(listSize) {
            game.copy(setId = differentSetId, gameId = defaultUUID(randomize = true))
        }

        gameList.forEach { gameInList ->
            val insertion = gameDataSource.insertOrReplaceGameForSet(
                gameId = gameInList.gameId,
                setId = gameInList.setId,
                serverPoints = gameInList.serverPoints,
                receiverPoints = gameInList.receiverPoints,
            )
            assertThat(insertion is Result.Success).isTrue()
        }

        gameListNotToBeRetrieved.forEach { gameInList ->
            val insertion = gameDataSource.insertOrReplaceGameForSet(
                gameId = gameInList.gameId,
                setId = gameInList.setId,
                serverPoints = gameInList.serverPoints,
                receiverPoints = gameInList.receiverPoints,
            )
            assertThat(insertion is Result.Success).isTrue()
        }


        val result = gameDataSource.getGameListForSet(game.setId)

        assertThat(result).isEqualTo(gameList)
        assertThat(result.size).isEqualTo(listSize)
    }
}