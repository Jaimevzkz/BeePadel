package com.vzkz.core.database.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.vzkz.core.database.data.mappers.toDbDomain
import com.vzkz.core.database.domain.GameDataSource
import com.vzkz.core.database.domain.MatchDataSource
import com.vzkz.core.database.domain.SetDataSource
import com.vzkz.core.database.domain.model.MatchEntityModel
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration

class MatchDataSourceImpl(
    private val db: BeePadelDB,
    private val dispatchers: DispatchersProvider,
    private val setDataSource: SetDataSource,
    private val gameDataSource: GameDataSource,
) : MatchDataSource {
    private val queries = db.matchQueries

    override fun getMatchListAsFlow(): Flow<List<MatchEntityModel>> {
        return queries
            .getAllMatches()
            .asFlow()
            .mapToList(dispatchers.io)
            .map { entityList -> entityList.map { it.toDbDomain() } }
    }

    override suspend fun insertOrReplaceMatch(
        matchId: UUID,
        dateTimeUtc: ZonedDateTime,
        elapsedTime: Duration
    ): Result<Unit, DataError.Local> {
        val insert = queries.insertOrReplaceMatch(
            matchId = matchId,
            dateTimeUtc = dateTimeUtc,
            elapsedTime = elapsedTime
        )
        return if (insert.value.toInt() != 1) Result.Error(DataError.Local.INSERT_MATCH_FAILED)
        else Result.Success(Unit)
    }

    override suspend fun deleteMatchById(matchId: UUID): Result<Unit, DataError.Local> {
        val delete = queries.deleteMatchById(
            id = matchId
        )
        return if (delete.value.toInt() != 1) Result.Error(DataError.Local.DELETE_MATCH_FAILED)
        else Result.Success(Unit)
    }
}