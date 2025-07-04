package com.vzkz.core.database.domain

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import kotlinx.coroutines.flow.Flow
import match.MatchEntity
import set.SetEntity
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration

interface SetDataSource {
    fun getSetListForMatch(matchId: UUID): List<SetEntity>

    suspend fun insertOrReplaceSetForMatch(setId: UUID, matchId: UUID): Result<Unit, DataError.Local>

    suspend fun deleteSetsWithMatchId(matchId: UUID): Result<Unit, DataError.Local>

}