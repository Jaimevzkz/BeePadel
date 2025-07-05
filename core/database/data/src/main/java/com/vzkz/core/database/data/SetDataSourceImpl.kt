package com.vzkz.core.database.data

import com.vzkz.core.database.data.mappers.toDbDomain
import com.vzkz.core.database.domain.SetDataSource
import com.vzkz.core.database.domain.model.SetEntityModel
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import set.SetEntity
import java.util.UUID

class SetDataSourceImpl(
    db: BeePadelDB,
) : SetDataSource {
    private val queries = db.setQueries
    override suspend fun getSetListForMatch(matchId: UUID): List<SetEntityModel> {
        return queries.getSetListWithMatchId(matchId).executeAsList().map { it.toDbDomain() }
    }

    override suspend fun getSetIdList(matchId: UUID): List<UUID>{
        return queries.getSetIdList(matchId).executeAsList()
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