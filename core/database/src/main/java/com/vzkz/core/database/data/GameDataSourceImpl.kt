package com.vzkz.core.database.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.vzkz.core.database.BeePadelDB
import com.vzkz.core.database.domain.GameDataSource
import com.vzkz.core.database.domain.MatchDataSource
import com.vzkz.core.database.domain.SetDataSource
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Points
import game.GameEntity
import kotlinx.coroutines.flow.Flow
import match.MatchEntity
import set.SetEntity
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration

class GameDataSourceImpl(
    db: BeePadelDB,
    private val dispatchers: DispatchersProvider
) : GameDataSource {
    private val queries = db.gameQueries
    override fun getSetListForMatch(setId: UUID): List<GameEntity> {
        return queries.getGameListWithSetId(setId).executeAsList()
    }

    override suspend fun insertOrReplaceGameForSet(
        gameId: UUID,
        setId: UUID,
        serverPoints: Points,
        receiverPoints: Points
    ): Result<Unit, DataError.Local> {
        val insert = queries.insertOrReplaceGame(
            gameId = gameId,
            setId = setId,
            serverPoints = serverPoints,
            receiverPoints = receiverPoints
        )

        return if (insert.value.toInt() != 1) Result.Error(DataError.Local.INSERT_MATCH_FAILED)
        else Result.Success(Unit)
    }

    override suspend fun deleteSetsWithSetId(setId: UUID): Result<Unit, DataError.Local> {
        val delete = queries.deleteAllSetsFromMatch(setId)

        return if (delete.value.toInt() == 0) Result.Error(DataError.Local.DELETE_MATCH_FAILED)
        else Result.Success(Unit)
    }
}