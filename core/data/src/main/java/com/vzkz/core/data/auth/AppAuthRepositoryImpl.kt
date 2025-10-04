package com.vzkz.core.data.auth

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.vzkz.core.data.BuildConfig
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.SessionStorage
import com.vzkz.core.domain.auth.AuthInfo
import com.vzkz.core.domain.auth.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.getValue

class AppAuthRepositoryImpl(
    private val authService: AuthorizationService,
    private val sessionStorage: SessionStorage,
    private val dispatchers: DispatchersProvider
): AuthRepository {
    override suspend fun isLoggedIn() = loadAuthState() != null

    override suspend fun <T> withFreshAccessToken(action: suspend (accessToken: String) -> T): T {
        val state = loadAuthState() ?: throw IllegalStateException("Not signed in")
        return suspendCancellableActionWithAuthState(state, action)
    }

    private suspend fun loadAuthState(): AuthState? {
        val authInfo = sessionStorage.get() ?: return null
        return try {
            AuthState.jsonDeserialize(org.json.JSONObject(authInfo.authState))
        } catch (e: Exception) {
            null
        }
    }

    private suspend fun <T> suspendCancellableActionWithAuthState(
        authState: AuthState,
        action: suspend (String) -> T
    ): T = suspendCancellableCoroutine { continuation ->
        authState.performActionWithFreshTokens(authService) { accessToken, idToken, exception ->
            if (exception != null) {
                continuation.resumeWithException(exception)
                return@performActionWithFreshTokens
            }
            if (accessToken == null) {
                continuation.resumeWithException(IllegalStateException("accessToken missing"))
                return@performActionWithFreshTokens
            }

            // Launch the Ktor call (action)
            val job = CoroutineScope(dispatchers.io).launch {
                try {
                    val result =
                        action(accessToken) //TODO Change this to return a result (Network errors) (?)
                    continuation.resume(result)
                } catch (e: Throwable) {
                    continuation.resumeWithException(e)
                }
            }
            continuation.invokeOnCancellation { job.cancel() }
        }
    }
}