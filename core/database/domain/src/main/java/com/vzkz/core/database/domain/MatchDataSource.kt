package com.vzkz.core.database.domain

import com.vzkz.core.database.domain.model.MatchEntityModel
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration

interface MatchDataSource {
    fun getMatchListAsFlow(): Flow<List<MatchEntityModel>>

    suspend fun insertOrReplaceMatch(
        matchId: UUID,
        dateTimeUtc: ZonedDateTime,
        elapsedTime: Duration
    ): Result<Unit, DataError.Local>

    suspend fun deleteMatchById(matchId: UUID): Result<Unit, DataError.Local>
}
