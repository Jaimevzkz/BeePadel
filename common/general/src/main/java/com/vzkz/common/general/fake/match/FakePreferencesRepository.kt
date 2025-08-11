package com.vzkz.common.general.fake.match

import com.vzkz.beepadel.core.preferences.domain.PreferencesRepository
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.core.domain.error.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakePreferencesRepository : PreferencesRepository {

    private val booleanPreferences = MutableStateFlow(mapOf<String, Boolean>())

    override suspend fun storeBooleanPreference(
        key: String,
        value: Boolean
    ): EmptyResult<DataError.Local> {
        val newMap = booleanPreferences.value.toMutableMap().apply { this[key] = value }
        booleanPreferences.value = newMap
        return Result.Success(Unit)
    }

    override suspend fun getBooleanPreference(key: String): Boolean? {
        return booleanPreferences.value[key]
    }

    override fun getBooleanPreferenceAsFlow(key: String): Flow<Boolean?> {
        return booleanPreferences.map { it[key] }
    }
}