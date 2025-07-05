package com.vzkz.match.domain.active_match

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Match

interface ActiveMatchRepository {
    suspend fun insertMatch(match: Match): Result<Unit, DataError.Local>
}