package com.vzkz.common.general.fake.database

import com.vzkz.core.database.domain.GameDataSource
import com.vzkz.core.database.domain.model.GameEntityModel
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Points
import java.util.UUID

class FakeGameDataSource : GameDataSource {

    private val gameList = mutableListOf<GameEntityModel>()

    var returnError: DataError.Local? = null

    override suspend fun getGameListForSet(setId: UUID): List<GameEntityModel> {
        return gameList.filter { it.setId == setId }
    }

    override suspend fun insertOrReplaceGameForSet(
        gameId: UUID,
        setId: UUID,
        serverPoints: Points,
        receiverPoints: Points
    ): Result<Unit, DataError.Local> {
        if (returnError != null) return Result.Error(returnError!!)

        val existingIndex = gameList.indexOfFirst { it.gameId == gameId }
        val newGame = GameEntityModel(
            gameId = gameId,
            setId = setId,
            serverPoints = serverPoints,
            receiverPoints = receiverPoints
        )

        if (existingIndex != -1) {
            gameList[existingIndex] = newGame
        } else {
            gameList.add(newGame)
        }

        return Result.Success(Unit)
    }

    override suspend fun deleteGamesWithSetId(setId: UUID): Result<Unit, DataError.Local> {
        if (returnError != null) return Result.Error(returnError!!)

        val remove = gameList.removeIf { it.setId == setId }

        if (!remove) return Result.Error(DataError.Local.DELETE_MATCH_FAILED)

        return Result.Success(Unit)
    }
}