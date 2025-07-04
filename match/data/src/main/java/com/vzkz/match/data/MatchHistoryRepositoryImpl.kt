package com.vzkz.match.data

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.match.domain.MatchHistoryRepository
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class MatchHistoryRepositoryImpl: MatchHistoryRepository {
    override fun getMatchHistory(): Flow<List<Match>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMatch(matchId: UUID): EmptyResult<DataError.Local> {
        TODO("Not yet implemented")
    }
}