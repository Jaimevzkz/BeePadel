package com.vzkz.core.database.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.vzkz.core.database.BeePadelDB
import com.vzkz.core.database.domain.MatchDataSource
import com.vzkz.core.database.domain.SetDataSource
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import kotlinx.coroutines.flow.Flow
import match.MatchEntity
import set.SetEntity
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.time.Duration

class SetDataSourceImpl(
    db: BeePadelDB,
    private val dispatchers: DispatchersProvider
) : SetDataSource {
    private val queries = db.setQueries
    override fun getSetListForMatch(matchId: UUID): List<SetEntity> {
        return queries.getSetListWithMatchId(matchId).executeAsList()
    }

    override suspend fun insertOrReplaceSetForMatch(
        setId: UUID,
        matchId: UUID
    ): Result<Unit, DataError.Local> {
        val insert = queries.insertOrReplaceSet(setId = setId, matchId = matchId)

        return if (insert.value.toInt() != 1) Result.Error(DataError.Local.INSERT_MATCH_FAILED)
        else Result.Success(Unit)
    }

    override suspend fun deleteSetsWithMatchId(matchId: UUID): Result<Unit, DataError.Local> {
        val delete = queries.deleteAllSetsFromMatch(matchId)

        return if (delete.value.toInt() == 0) Result.Error(DataError.Local.DELETE_MATCH_FAILED)
        else Result.Success(Unit)
    }
}