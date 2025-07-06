package com.vzkz.core.database.data

import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Points
import game.GameEntity
import kotlinx.coroutines.withContext
import java.util.UUID

class GameDataSource(
    db: BeePadelDB,
    private val dispatchers: DispatchersProvider
) {
    private val queries = db.gameQueries
    suspend fun getGameListForSet(setId: UUID): List<GameEntity> {
        return withContext(dispatchers.io) { queries.getGameListWithSetId(setId).executeAsList() }
    }

    fun insertOrReplaceGameForSet(
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

    fun deleteGamesWithSetId(setId: UUID): Result<Unit, DataError.Local> {
        val delete = queries.deleteAllGamesFromSet(setId)

        return if (delete.value.toInt() == 0) Result.Error(DataError.Local.DELETE_MATCH_FAILED)
        else Result.Success(Unit)
    }
}