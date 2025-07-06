package com.vzkz.match.data.active_match

import com.vzkz.core.database.domain.LocalStorageRepository
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.active_match.ActiveMatchRepository
import com.vzkz.match.domain.model.Match

class ActiveMatchRepositoryImpl(
    private val localStorageRepository: LocalStorageRepository
): ActiveMatchRepository {
    override suspend fun insertMatch(match: Match): Result<Unit, DataError.Local> {
        return localStorageRepository.insertOrReplaceMatch(match)
    }
}