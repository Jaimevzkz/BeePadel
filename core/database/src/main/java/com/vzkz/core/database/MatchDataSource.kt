package com.vzkz.core.database

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import match.MatchEntity

interface MatchDataSource {
    fun getMatchListAsFlow(): Flow<List<MatchEntity>>

    suspend fun insertOrReplaceMatch(match: Match): Result<Unit, DataError.Local>

    suspend fun deleteMatchById(matchId: String): Result<Unit, DataError.Local>
}