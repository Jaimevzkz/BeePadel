package com.vzkz.core.database.domain

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Points
import game.GameEntity
import kotlinx.coroutines.flow.Flow
import match.MatchEntity
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration

interface GameDataSource {
    fun getSetListForMatch(setId: UUID): List<GameEntity>

    suspend fun insertOrReplaceGameForSet(gameId: UUID, setId: UUID, serverPoints: Points, receiverPoints: Points): Result<Unit, DataError.Local>

    suspend fun deleteSetsWithSetId(setId: UUID): Result<Unit, DataError.Local>

}