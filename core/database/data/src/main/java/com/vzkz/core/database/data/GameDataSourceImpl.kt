package com.vzkz.core.database.data

import com.vzkz.core.database.data.mappers.toDbDomain
import com.vzkz.core.database.domain.GameDataSource
import com.vzkz.core.database.domain.model.GameEntityModel
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Points
import java.util.UUID

class GameDataSourceImpl(
    db: BeePadelDB,
) : GameDataSource {
    private val queries = db.gameQueries
    override suspend fun getGameListForSet(setId: UUID): List<GameEntityModel> {
        return queries.getGameListWithSetId(setId).executeAsList().map { it.toDbDomain() }
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

    override suspend fun deleteGamesWithSetId(setId: UUID): Result<Unit, DataError.Local> {
        val delete = queries.deleteAllGamesFromSet(setId)

        return if (delete.value.toInt() == 0) Result.Error(DataError.Local.DELETE_MATCH_FAILED)
        else Result.Success(Unit)
    }
}