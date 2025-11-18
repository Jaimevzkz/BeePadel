package com.vzkz.core.data

import com.vzkz.core.data.auth.token.RefreshTokenRequest
import com.vzkz.core.data.auth.token.RefreshTokenResponse
import com.vzkz.core.data.networking.DEAUTHORIZE
import com.vzkz.core.data.networking.TOKEN
import com.vzkz.core.data.networking.post
import com.vzkz.core.domain.SessionStorage
import com.vzkz.core.domain.auth.AuthInfo
import com.vzkz.core.domain.auth.AuthRepository
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.core.domain.error.Result
import com.vzkz.core.domain.error.asEmptyDataResult
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.InternalSerializationApi
import timber.log.Timber

@OptIn(InternalSerializationApi::class)
class AppAuthRepositoryImpl(
    private val sessionStorage: SessionStorage,
    private val httpClient: HttpClient,
) : AuthRepository {
    override val isLoggedIn: StateFlow<Boolean>
        get() = sessionStorage.tokensAvailable

    override suspend fun fetchAndSaveRefreshToken(code: String): EmptyResult<DataError.Network> {
        val result = httpClient.post<RefreshTokenRequest, RefreshTokenResponse>(
            route = TOKEN,
            body = RefreshTokenRequest(
                    client_id = BuildConfig.STRAVA_CLIENT_ID,
                    client_secret = BuildConfig.STRAVA_CLIENT_SECRET,
                    code = code,
                    grant_type = "authorization_code"
                )
        )

        if (result is Result.Success) {
            sessionStorage.set(
                AuthInfo(
                    accessToken = result.data.access_token,
                    refreshToken = result.data.refresh_token,
                )
            )
        }
        return result.asEmptyDataResult()
    }

    override suspend fun logoutFromStrava(): EmptyResult<DataError.Network>{
        val result = httpClient.post<Unit, Unit>(
            route = DEAUTHORIZE,
            body = Unit
        )
        if (result is Result.Success)
            sessionStorage.set(null)

        return result.asEmptyDataResult()

    }
}