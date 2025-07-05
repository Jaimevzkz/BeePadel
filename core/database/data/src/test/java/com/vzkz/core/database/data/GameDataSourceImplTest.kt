package com.vzkz.core.database.data

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.vzkz.common.general.data_generator.gameEntityModel
import com.vzkz.core.database.domain.model.GameEntityModel
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Points
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.math.exp

@OptIn(ExperimentalCoroutinesApi::class)
class GameDataSourceImplTest : DatabaseTest() {

    // SUT
    private lateinit var gameDataSourceImpl: GameDataSourceImpl

    @BeforeEach
    fun individualSetUp() {
        gameDataSourceImpl = GameDataSourceImpl(
            db = beePadelDB
        )
    }

    @Test
    fun `creating a game`() = runTest {
        val expectedGame = gameEntityModel()

        gameDataSourceImpl.insertOrReplaceGameForSet(
            gameId = expectedGame.gameId,
            setId = expectedGame.setId,
            serverPoints = expectedGame.serverPoints,
            receiverPoints = expectedGame.receiverPoints
        )
        runCurrent()

        val result = gameDataSourceImpl.getGameListForSet(
            setId = expectedGame.setId
        )
        assertThat(result).isEqualTo(listOf(expectedGame))
    }

    @Test
    fun `Inserting multiple games with same id just updates them`() = runTest {
        val game = gameEntityModel()
        val expectedGameListSize = 1

        (0..<3).forEach { _ ->
            gameDataSourceImpl.insertOrReplaceGameForSet(
                gameId = game.gameId,
                setId = game.setId,
                serverPoints = game.serverPoints,
                receiverPoints = game.receiverPoints
            )
        }

        val resultingListSize = gameDataSourceImpl.getGameListForSet(game.setId).size

        assertThat(resultingListSize).isEqualTo(expectedGameListSize)
    }

    @Test
    fun `Deleting multiple games with the same set id works`() = runTest {
        val game = gameEntityModel()
        var expectedGameListSize = 4

        (0..<expectedGameListSize).forEach { _ ->
            gameDataSourceImpl.insertOrReplaceGameForSet(
                gameId = UUID.randomUUID(),
                setId = game.setId,
                serverPoints = game.serverPoints,
                receiverPoints = game.receiverPoints
            )
        }
        var resultingSize = gameDataSourceImpl.getGameListForSet(game.setId).size

        assertThat(resultingSize).isEqualTo(expectedGameListSize)

        expectedGameListSize = 0
        gameDataSourceImpl.deleteGamesWithSetId(game.setId)
        resultingSize = gameDataSourceImpl.getGameListForSet(game.setId).size

        assertThat(resultingSize).isEqualTo(expectedGameListSize)
    }

    @Test
    fun `inserting multiple games and the getting them`() = runTest {
        val game = gameEntityModel()
        val listSize = 5
        val gameList = List(listSize) {
            game.copy(gameId = UUID.randomUUID())
        }

        val differentSetId = UUID.randomUUID()
        val gameListNotToBeRetrieved = List(listSize) {
            game.copy(setId = differentSetId, gameId = UUID.randomUUID())
        }

        gameList.forEach { gameInList ->
            val insertion = gameDataSourceImpl.insertOrReplaceGameForSet(
                gameId = gameInList.gameId,
                setId = gameInList.setId,
                serverPoints = gameInList.serverPoints,
                receiverPoints = gameInList.receiverPoints,
            )
            assertThat(insertion is Result.Success).isTrue()
        }

        gameListNotToBeRetrieved.forEach { gameInList ->
            val insertion = gameDataSourceImpl.insertOrReplaceGameForSet(
                gameId = gameInList.gameId,
                setId = gameInList.setId,
                serverPoints = gameInList.serverPoints,
                receiverPoints = gameInList.receiverPoints,
            )
            assertThat(insertion is Result.Success).isTrue()
        }


        val result = gameDataSourceImpl.getGameListForSet(game.setId)

        assertThat(result).isEqualTo(gameList)
        assertThat(result.size).isEqualTo(listSize)
    }
}