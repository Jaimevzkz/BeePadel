package com.vzkz.common.general.fake.database

import com.vzkz.core.database.domain.SetDataSource
import com.vzkz.core.database.domain.model.SetEntityModel
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import java.util.UUID

class FakeSetDataSource : SetDataSource {

    private val setList = mutableListOf<SetEntityModel>()

    var returnError: DataError.Local? = null

    override suspend fun getSetListForMatch(matchId: UUID): List<SetEntityModel> {
        return setList.filter { it.matchId == matchId }
    }

    override suspend fun insertOrReplaceSetForMatch(
        setId: UUID,
        matchId: UUID
    ): Result<Unit, DataError.Local> {
        if (returnError != null) return Result.Error(returnError!!)

        val existingIndex = setList.indexOfFirst { it.setId == setId }
        val newSet = SetEntityModel(
            setId = setId,
            matchId = matchId
        )

        if (existingIndex != -1) {
            setList[existingIndex] = newSet
        } else {
            setList.add(newSet)
        }

        return Result.Success(Unit)

    }

    override suspend fun deleteSetsWithMatchId(matchId: UUID): Result<Unit, DataError.Local> {
        if (returnError != null) return Result.Error(returnError!!)

        val remove = setList.removeIf { it.matchId == matchId }

        if (!remove) return Result.Error(DataError.Local.DELETE_SET_FAILED)

        return Result.Success(Unit)
    }

    override suspend fun getSetIdList(matchId: UUID): List<UUID> {
        return setList.filter { it.matchId == matchId }.map { it.setId }
    }


}