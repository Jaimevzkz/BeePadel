package com.vzkz.core.database.domain

import com.vzkz.core.database.domain.model.SetEntityModel
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import java.util.UUID

interface SetDataSource {
    suspend fun getSetListForMatch(matchId: UUID): List<SetEntityModel>

    suspend fun insertOrReplaceSetForMatch(
        setId: UUID,
        matchId: UUID
    ): Result<Unit, DataError.Local>

    suspend fun deleteSetsWithMatchId(matchId: UUID): Result<Unit, DataError.Local>

    suspend fun getSetIdList(matchId: UUID): List<UUID>
}