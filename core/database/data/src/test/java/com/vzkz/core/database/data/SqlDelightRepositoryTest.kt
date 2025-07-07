package com.vzkz.core.database.data

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import com.vzkz.common.general.data_generator.generateSet
import com.vzkz.common.general.data_generator.match
import com.vzkz.core.database.data.datasource.GameDataSource
import com.vzkz.core.database.data.datasource.MatchDataSource
import com.vzkz.core.database.data.datasource.SetDataSource
import com.vzkz.core.database.data.util.DatabaseTest
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SqlDelightRepositoryTest : DatabaseTest() {

    // SUT
    private lateinit var sqlDelightRepository: SqlDelightRepository

    private lateinit var matchDataSource: MatchDataSource
    private lateinit var setDataSource: SetDataSource
    private lateinit var gameDataSource: GameDataSource

    @BeforeEach
    fun individualSetUp() {
        matchDataSource = MatchDataSource(
            db = beePadelDB,
            dispatchers = testDispatchers
        )
        setDataSource = SetDataSource(
            db = beePadelDB,
            dispatchers = testDispatchers
        )
        gameDataSource = GameDataSource(
            db = beePadelDB,
            dispatchers = testDispatchers
        )

        sqlDelightRepository = SqlDelightRepository(
            localDB = beePadelDB,
            matchDataSource = matchDataSource,
            setDataSource = setDataSource,
            gameDataSource = gameDataSource,
            dispatchers = testDispatchers
        )
    }

    @Test
    fun `When inserting matches, the flow is updated correctly`() = runTest {
        val firstMatchToInsert = match()
        val secondMatchToInsert =
            match().copy(setList = listOf(generateSet(2, 6), generateSet(5, 7)))

        val matchFlow = sqlDelightRepository.getMatchHistory()

        matchFlow.test {
            val firstEmission = awaitItem()
            assertThat(firstEmission).isEqualTo(emptyList())

            sqlDelightRepository.insertOrReplaceMatch(firstMatchToInsert)
            val secondEmission = awaitItem()
            assertThat(secondEmission).isEqualTo(listOf(firstMatchToInsert))

            sqlDelightRepository.insertOrReplaceMatch(secondMatchToInsert)
            val thirdEmission = awaitItem()
            assertThat(thirdEmission).isEqualTo(listOf(firstMatchToInsert, secondMatchToInsert))
        }
    }

    @Test
    fun `When deleting matches, the flow is updated correctly`() = runTest {
        val firstMatchToInsert = match()
        val secondMatchToInsert =
            match().copy(setList = listOf(generateSet(2, 6), generateSet(5, 7)))

        val matchFlow = sqlDelightRepository.getMatchHistory()

        matchFlow.test {
            val firstEmission = awaitItem()
            assertThat(firstEmission).isEqualTo(emptyList())

            sqlDelightRepository.insertOrReplaceMatch(firstMatchToInsert)
            awaitItem()

            sqlDelightRepository.insertOrReplaceMatch(secondMatchToInsert)
            awaitItem()

            sqlDelightRepository.deleteMatch(firstMatchToInsert.matchId)
            val fourthEmission = awaitItem()
            assertThat(fourthEmission).isEqualTo(listOf(secondMatchToInsert))

            sqlDelightRepository.deleteMatch(secondMatchToInsert.matchId)
            val fifthEmission = awaitItem()
            assertThat(fifthEmission).isEqualTo(listOf())
        }
    }

    @Test
    fun `Deleting a match that doesn't exist results in error`() = runTest {
        val firstMatchToInsert = match()

        val matchFlow = sqlDelightRepository.getMatchHistory()

        matchFlow.test {
            val firstEmission = awaitItem()
            assertThat(firstEmission).isEqualTo(emptyList())

            val delete = sqlDelightRepository.deleteMatch(firstMatchToInsert.matchId)
            assertThat(delete is Result.Error).isTrue()
            assertThat((delete as Result.Error).error).isEqualTo(DataError.Local.DELETE_MATCH_FAILED)
        }
    }
}







