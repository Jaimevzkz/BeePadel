package com.vzkz.core.data

import android.content.SharedPreferences
import com.vzkz.core.data.auth.AuthInfoSerializable
import com.vzkz.core.data.auth.toAuthInfo
import com.vzkz.core.data.auth.toAuthInfoSerializable
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.SessionStorage
import com.vzkz.core.domain.auth.AuthInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@OptIn(InternalSerializationApi::class)
class EncryptedSessionStorage(
    private val sharedPreferences: SharedPreferences,
    private val dispatchers: DispatchersProvider,
    private val applicationScope: CoroutineScope
) : SessionStorage {
    private val _tokensAvailable = MutableStateFlow(false)
    override val tokensAvailable: StateFlow<Boolean>
        get() = stringFlow(KEY_AUTH_INFO).map { it != null }
            .stateIn(
                applicationScope,
                SharingStarted.Eagerly,
                initialValue = false
            )

    companion object {
        private const val KEY_AUTH_INFO = "KEY_AUTH_INFO"
    }

    override suspend fun get(): AuthInfo? {
        return withContext(dispatchers.io) {
            val json = sharedPreferences.getString(KEY_AUTH_INFO, null)
            json?.let {
                _tokensAvailable.value = true
                Json.Default.decodeFromString<AuthInfoSerializable>(it).toAuthInfo()
            }
        }
    }

    override suspend fun set(info: AuthInfo?) {
        withContext(dispatchers.io) {
            if (info == null) {
                _tokensAvailable.value = false
                sharedPreferences.edit(commit = true) { remove(KEY_AUTH_INFO) }
                return@withContext
            }
            val json = Json.Default.encodeToString(info.toAuthInfoSerializable())
            sharedPreferences
                .edit(commit = true) { putString(KEY_AUTH_INFO, json) }
            _tokensAvailable.value = true
        }

    }


    fun stringFlow(key: String, defaultValue: String? = null): Flow<String?> =
        callbackFlow {
            trySend(sharedPreferences.getString(key, defaultValue))

            val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, changedKey ->
                if (changedKey == key) {
                    trySend(prefs.getString(key, defaultValue))
                }
            }

            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

            awaitClose {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }
            .distinctUntilChanged()

}

