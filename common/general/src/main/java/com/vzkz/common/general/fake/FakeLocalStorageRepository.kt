package com.vzkz.common.general.fake

import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID

class FakeLocalStorageRepository: LocalStorageRepository {

    private val mathHistoryFlow = MutableStateFlow(mutableListOf<Match>())

    var errorToReturn: DataError.Local? = null

    override fun getMatchHistory(): Flow<List<Match>> {
        return mathHistoryFlow
    }

    override suspend fun deleteMatch(matchId: UUID): Result<Unit, DataError.Local> {
        if (errorToReturn != null) return Result.Error(errorToReturn!!)

        val matchHistoryList = mathHistoryFlow.value.toMutableList()
        val delete = matchHistoryList.removeIf { it.matchId == matchId }
        mathHistoryFlow.value = matchHistoryList

        if (!delete) return Result.Error(DataError.Local.DELETE_MATCH_FAILED)

        return Result.Success(Unit)
    }

    override suspend fun insertOrReplaceMatch(match: Match): Result<Unit, DataError.Local> {
        if (errorToReturn != null) return Result.Error(errorToReturn!!)

        val matchHistoryList = mathHistoryFlow.value.toMutableList()
        val indexOfMatch = matchHistoryList.indexOfFirst { it.matchId == match.matchId }
        if (indexOfMatch == -1) matchHistoryList.add(match)
        else matchHistoryList[indexOfMatch] = match

        mathHistoryFlow.value = matchHistoryList

        return Result.Success(Unit)
    }
}