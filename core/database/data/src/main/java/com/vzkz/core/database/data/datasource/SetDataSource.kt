package com.vzkz.core.database.data.datasource

import com.vzkz.core.database.data.BeePadelDB
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import kotlinx.coroutines.withContext
import set.SetEntity
import java.util.UUID

class SetDataSource(
    db: BeePadelDB,
    private val dispatchers: DispatchersProvider
) {
    private val queries = db.setQueries
    suspend fun getSetListForMatch(matchId: UUID): List<SetEntity> {
        return withContext(dispatchers.io) {
            queries.getSetListWithMatchId(matchId).executeAsList()
        }
    }

    suspend fun getSetIdList(matchId: UUID): List<UUID> {
        return withContext(dispatchers.io) { queries.getSetIdList(matchId).executeAsList() }
    }

    fun insertOrReplaceSetForMatch(
        setId: UUID,
        matchId: UUID
    ): Result<Unit, DataError.Local> {
        val insert = queries.insertOrReplaceSet(setId = setId, matchId = matchId)

        return if (insert.value.toInt() != 1) Result.Error(DataError.Local.INSERT_MATCH_FAILED)
        else Result.Success(Unit)
    }

    fun deleteSetsWithMatchId(matchId: UUID): Result<Unit, DataError.Local> {
        val delete = queries.deleteAllSetsFromMatch(matchId)

        return if (delete.value.toInt() == 0) Result.Error(DataError.Local.DELETE_MATCH_FAILED)
        else Result.Success(Unit)
    }
}