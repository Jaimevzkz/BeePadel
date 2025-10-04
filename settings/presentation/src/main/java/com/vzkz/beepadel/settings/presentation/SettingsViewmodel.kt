package com.vzkz.beepadel.settings.presentation

import android.app.PendingIntent
import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.vzkz.beepadel.core.preferences.domain.PreferencesRepository
import com.vzkz.common.general.GOLDEN_POINT
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.auth.AuthRepository
import com.vzkz.core.presentation.ui.BaseViewModel
import com.vzkz.core.presentation.ui.model.Intent
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import timber.log.Timber

class SettingsViewModel(
    private val dispatchers: DispatchersProvider,
    private val preferencesRepository: PreferencesRepository,
    private val authService: AuthorizationService,
    private val authRepository: AuthRepository
) : BaseViewModel<SettingsState, SettingsIntent, SettingsEvent>(
    SettingsState.initial,
    dispatchers
) {
    init {
        viewModelScope.launch(dispatchers.io) {
            val isLoggedIn = authRepository.isLoggedIn()
            _state.update { it.copy(isLoggedIntoStrava = isLoggedIn) }
        }

        preferencesRepository
            .getBooleanPreferenceAsFlow(GOLDEN_POINT.KEY)
            .onEach { newValue ->
                _state.update {
                    it.copy(goldenPoint = newValue ?: GOLDEN_POINT.DEFAULT_VAL)
                }
            }
            .flowOn(dispatchers.io)
            .launchIn(viewModelScope)

    }

    override fun reduce(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.NavigateBack -> sendEvent(SettingsEvent.NavigateBack)

            SettingsIntent.ToggleGoldenPoint -> ioLaunch {
                preferencesRepository.storeBooleanPreference(
                    GOLDEN_POINT.KEY, !state.value.goldenPoint
                )
            }

            SettingsIntent.LaunchAuthRequestIntent ->
                sendEvent(SettingsEvent.LaunchAuthRequestIntent(createAuthRequestIntent()))
        }
    }


    private fun createAuthRequestIntent(): android.content.Intent {
        val scope = "activity:read,activity:write"
        val clientId = com.vzkz.core.presentation.ui.BuildConfig.STRAVA_CLIENT_ID
        val redirectUri = "beepadel://oauth2redirect".toUri()
        val serviceConfig = AuthorizationServiceConfiguration(
            /* authorizationEndpoint = */ "https://www.strava.com/oauth/authorize".toUri(),
            /* tokenEndpoint = */ "https://www.strava.com/oauth/token".toUri()
        )

        val authRequest = AuthorizationRequest.Builder(
            serviceConfig,
            clientId,
            ResponseTypeValues.CODE,
            redirectUri
        ).setScopes(scope)
            .build()

        return authService.getAuthorizationRequestIntent(authRequest)
    }
}
