package com.vzkz.core.database.domain

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface LocalStorageRepository {
    fun getMatchHistory(): Flow<List<Match>>

    suspend fun deleteMatch(matchId: UUID): Result<Unit, DataError.Local>

    suspend fun insertOrReplaceMatch(match: Match): Result<Unit, DataError.Local>
}