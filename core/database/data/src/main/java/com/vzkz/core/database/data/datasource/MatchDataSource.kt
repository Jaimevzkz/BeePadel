package com.vzkz.core.database.data.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.vzkz.core.database.data.BeePadelDB
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import kotlinx.coroutines.flow.Flow
import match.MatchEntity
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.math.max
import kotlin.time.Duration

class MatchDataSource(
    db: BeePadelDB,
    private val dispatchers: DispatchersProvider,
) {
    private val queries = db.matchQueries

    fun getMatchListAsFlow(): Flow<List<MatchEntity>> {
        return queries
            .getAllMatches()
            .asFlow()
            .mapToList(dispatchers.io)
    }

    fun insertOrReplaceMatch(
        matchId: UUID,
        dateTimeUtc: ZonedDateTime,
        elapsedTime: Duration,
        avgHeartRate: Int?,
        maxHeartRate: Int?
    ): Result<Unit, DataError.Local> {
        val insert = queries.insertOrReplaceMatch(
            matchId = matchId,
            dateTimeUtc = dateTimeUtc,
            elapsedTime = elapsedTime,
            avgHeartRate = avgHeartRate?.toLong(),
            maxHeartRate = maxHeartRate?.toLong()
        )

        return if (insert.value.toInt() != 1) Result.Error(DataError.Local.INSERT_MATCH_FAILED)
        else Result.Success(Unit)
    }

    fun deleteMatchById(matchId: UUID): Result<Unit, DataError.Local> {
        val delete = queries.deleteMatchById(
            id = matchId
        )
        return if (delete.value.toInt() != 1) Result.Error(DataError.Local.DELETE_MATCH_FAILED)
        else Result.Success(Unit)
    }
}