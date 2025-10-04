package com.vzkz.core.data.auth

import android.content.SharedPreferences
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.SessionStorage
import com.vzkz.core.domain.auth.AuthInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(InternalSerializationApi::class)
class EncryptedSessionStorage(
    private val sharedPreferences: SharedPreferences,
    private val dispatchers: DispatchersProvider
) : SessionStorage {
    companion object {
        private const val KEY_AUTH_INFO = "KEY_AUTH_INFO"
    }

    override suspend fun get(): AuthInfo? {
        return withContext(dispatchers.io) {
            val json = sharedPreferences.getString(KEY_AUTH_INFO, null)
            json?.let {
                Json.decodeFromString<AuthInfoSerializable>(it).toAuthInfo()
            }
        }
    }

    override suspend fun set(info: AuthInfo?) {
        withContext(dispatchers.io) {
            if (info == null) {
                sharedPreferences.edit().remove(KEY_AUTH_INFO).commit()
                return@withContext
            }
            val json = Json.encodeToString(info.toAuthInfoSerializable())
            sharedPreferences
                .edit()
                .putString(KEY_AUTH_INFO, json)
                .commit()
        }

    }
}