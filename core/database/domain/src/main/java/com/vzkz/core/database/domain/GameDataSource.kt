package com.vzkz.core.database.domain

import com.vzkz.core.database.domain.model.GameEntityModel
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Points
import java.util.UUID

interface GameDataSource {
    suspend fun getGameListForSet(setId: UUID): List<GameEntityModel>

    suspend fun insertOrReplaceGameForSet(gameId: UUID, setId: UUID, serverPoints: Points, receiverPoints: Points): Result<Unit, DataError.Local>

    suspend fun deleteGamesWithSetId(setId: UUID): Result<Unit, DataError.Local>

}