package com.vzkz.beepadel

import android.content.Intent
import android.view.View
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.SessionStorage
import com.vzkz.core.domain.auth.AuthInfo
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.openid.appauth.AuthState
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.AuthorizationService
import timber.log.Timber

class MainViewmodel(
    private val sessionStorage: SessionStorage,
    private val dispatchers: DispatchersProvider,
    private val authService: AuthorizationService
): ViewModel() {

    fun handleAuthResponseIntent(intent: Intent) {
        Timber.tag("IN-APP").i("Extras: ${intent.extras}")
        val response = AuthorizationResponse.fromIntent(intent)
        val exception = AuthorizationException.fromIntent(intent)

        if (response == null) {
            Timber.tag("IN-APP").e("No auth response: ${exception?.errorDescription}")
            return
        }

        val tokenRequest = response.createTokenExchangeRequest()
        Timber.tag("IN-APP").i("Performed token exchange request: $tokenRequest")
        authService.performTokenRequest(tokenRequest) { tokenResponse, tokenEx ->
            Timber.tag("IN-APP").i("Received token request callback: $tokenResponse")
            if (tokenResponse != null) {
                val authState = AuthState(response, exception)
                authState.update(tokenResponse, tokenEx)
                viewModelScope.launch {
                    sessionStorage.set(AuthInfo(authState.jsonSerializeString()))
                    Timber.tag("IN-APP").i("Tokens saved: ${authState.accessToken}")
                }
            } else {
                Timber.tag("IN-APP").e("Token request failed: ${tokenEx?.errorDescription}")
            }
        }
    }


}