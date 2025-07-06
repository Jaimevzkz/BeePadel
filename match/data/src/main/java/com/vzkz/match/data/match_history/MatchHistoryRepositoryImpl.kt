package com.vzkz.match.data.match_history


import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.match.domain.match_history.MatchHistoryRepository
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class MatchHistoryRepositoryImpl(
    private val localStorageRepository: LocalStorageRepository,
) : MatchHistoryRepository {

    override fun getMatchHistory(): Flow<List<Match>> {
        return localStorageRepository.getMatchHistory()
    }

    override suspend fun deleteMatch(matchId: UUID): EmptyResult<DataError.Local> {
        return localStorageRepository.deleteMatch(matchId)
    }
}