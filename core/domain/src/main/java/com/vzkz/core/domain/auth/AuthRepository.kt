package com.vzkz.core.domain.auth

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.EmptyResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    val isLoggedIn: StateFlow<Boolean>

    suspend fun fetchAndSaveRefreshToken(code: String): EmptyResult<DataError.Network>
}