package com.vzkz.common.general.fake.database

import com.vzkz.core.database.domain.MatchDataSource
import com.vzkz.core.database.domain.model.MatchEntityModel
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration

class FakeMatchDataSource : MatchDataSource {

    private val matchListFlow = MutableStateFlow(mutableListOf<MatchEntityModel>())

    var returnError: DataError.Local? = null

    override fun getMatchListAsFlow(): Flow<List<MatchEntityModel>> {
        return matchListFlow
    }

    override suspend fun insertOrReplaceMatch(
        matchId: UUID,
        dateTimeUtc: ZonedDateTime,
        elapsedTime: Duration
    ): Result<Unit, DataError.Local> {
        if (returnError != null) return Result.Error(returnError!!)

        val existingIndex = matchListFlow.value.indexOfFirst { it.matchId == matchId }
        val newMatch = MatchEntityModel(
            matchId = matchId,
            dateTimeUtc = dateTimeUtc,
            elapsedTime = elapsedTime
        )

        if (existingIndex != -1) {
            val newMatchList = matchListFlow.value.toMutableList()
            newMatchList[existingIndex] = newMatch
            matchListFlow.value = newMatchList
        } else {
            val newMatchList = matchListFlow.value.toMutableList()
            newMatchList.add(newMatch)
            matchListFlow.value = newMatchList
        }

        return Result.Success(Unit)
    }

    override suspend fun deleteMatchById(matchId: UUID): Result<Unit, DataError.Local> {
        if (returnError != null) return Result.Error(returnError!!)

        val newMatchList = matchListFlow.value.toMutableList()
        val remove = newMatchList.removeIf { it.matchId == matchId }
        matchListFlow.value = newMatchList

        if (!remove) return Result.Error(DataError.Local.DELETE_MATCH_FAILED)

        return Result.Success(Unit)
    }

}