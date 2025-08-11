package com.vzkz.beepadel.core.preferences.domain

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.EmptyResult
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    suspend fun storeBooleanPreference(key: String, value: Boolean): EmptyResult<DataError.Local>
    suspend fun getBooleanPreference(key: String): Boolean?
    fun getBooleanPreferenceAsFlow(key: String): Flow<Boolean?>
}