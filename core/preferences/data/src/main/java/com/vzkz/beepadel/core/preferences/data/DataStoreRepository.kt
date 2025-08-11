package com.vzkz.beepadel.core.preferences.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.vzkz.beepadel.core.preferences.domain.PreferencesRepository
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.EmptyResult
import kotlinx.coroutines.flow.Flow
import com.vzkz.core.domain.error.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import timber.log.Timber


private const val PREFERENCES_NAME = "settings"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class DataStoreRepository(
    private val context: Context
) : PreferencesRepository {
    override suspend fun storeBooleanPreference(key: String, value: Boolean) = savePreference(booleanPreferencesKey(key), value)

    override suspend fun getBooleanPreference(key: String): Boolean? = getPreference(booleanPreferencesKey(key))

    override fun getBooleanPreferenceAsFlow(key: String): Flow<Boolean?> = getPreferenceAsFlow(booleanPreferencesKey(key))

    // Generic methods
    private suspend fun <T> savePreference(
        key: Preferences.Key<T>,
        value: T
    ): EmptyResult<DataError.Local> {
        return try {
            context.dataStore.edit { preferences -> preferences[key] = value }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    private suspend fun <T> getPreference(key: Preferences.Key<T>): T? =
        context.dataStore.data.first()[key]

    private fun <T> getPreferenceAsFlow(key: Preferences.Key<T>): Flow<T?> {
        return context.dataStore.data.map { it[key] }
    }
}