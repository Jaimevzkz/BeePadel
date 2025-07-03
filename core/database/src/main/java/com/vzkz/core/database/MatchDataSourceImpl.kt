package com.vzkz.core.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import match.MatchEntity

class MatchDataSourceImpl(
    db: BeePadelDB,
    private val dispatchers: DispatchersProvider
) : MatchDataSource {
    private val queries = db.matchQueries

    override fun getMatchListAsFlow(): Flow<List<MatchEntity>> {
        return queries
            .getAllMatches()
            .asFlow()
            .mapToList(dispatchers.io)
    }

    override suspend fun insertOrReplaceMatch(match: Match): Result<Unit, DataError.Local> {
        val insert = queries.insertOrReplaceMatch(
            matchId = match.matchId.toString(), // todo map this values correctly
            dateTimeUtc = match.dateTimeUtc.toString()
        )
        return if (insert.value.toInt() != 1) Result.Error(DataError.Local.INSERT_MATCH_FAILED)
        else Result.Success(Unit)
    }

    override suspend fun deleteMatchById(matchId: String): Result<Unit, DataError.Local> {
        val delete = queries.deleteMatchById(
            id = matchId
        )
        return if (delete.value.toInt() != 1) Result.Error(DataError.Local.DELETE_MATCH_FAILED)
        else Result.Success(Unit)
    }
}