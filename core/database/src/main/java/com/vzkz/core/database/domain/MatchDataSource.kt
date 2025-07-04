package com.vzkz.core.database.domain

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import kotlinx.coroutines.flow.Flow
import match.MatchEntity
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration

interface MatchDataSource {
    fun getMatchListAsFlow(): Flow<List<MatchEntity>>

    suspend fun insertOrReplaceMatch(matchId: UUID, dateTimeUtc: ZonedDateTime, elapsedTime: Duration): Result<Unit, DataError.Local>

    suspend fun deleteMatchById(matchId: UUID): Result<Unit, DataError.Local>
}
