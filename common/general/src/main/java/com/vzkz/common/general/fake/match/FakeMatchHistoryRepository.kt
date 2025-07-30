package com.vzkz.common.general.fake.match

import com.vzkz.common.general.data_generator.dummyMatchList
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.match_history.MatchHistoryRepository
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class FakeMatchHistoryRepository : MatchHistoryRepository {

    private val matchList: MutableStateFlow<List<Match>> =
        MutableStateFlow(dummyMatchList())

    override fun getMatchHistory(): Flow<List<Match>> {
        return matchList
    }

    override suspend fun deleteMatch(matchId: UUID): EmptyResult<DataError.Local> {
        val newMatchList = matchList.value.toMutableList()
        val removal = newMatchList.removeIf { it.matchId == matchId }

        if (!removal) return Result.Error(DataError.Local.DELETE_MATCH_FAILED)

        matchList.value = newMatchList
        return Result.Success(Unit)
    }
}