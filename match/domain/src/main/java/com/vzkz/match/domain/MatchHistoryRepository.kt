package com.vzkz.match.domain

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface MatchHistoryRepository {
    fun getMatchHistory(): Flow<List<Match>>

    suspend fun deleteMatch(matchId: UUID): EmptyResult<DataError.Local>
}