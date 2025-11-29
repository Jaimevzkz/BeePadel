package com.vzkz.beepadel.settings.presentation.strava_settings

import androidx.lifecycle.viewModelScope
import com.vzkz.beepadel.core.preferences.domain.PreferencesRepository
import com.vzkz.common.general.LOGGED_WITH_BEEPADEL
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.auth.AuthRepository
import com.vzkz.core.domain.error.Result
import com.vzkz.core.presentation.ui.BaseViewModel
import com.vzkz.core.presentation.ui.asUiText
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class StravaSettingsViewmodel(
    private val dispatchers: DispatchersProvider,
    private val authRepository: AuthRepository,
    private val preferencesRepository: PreferencesRepository,
) : BaseViewModel<StravaSettingsState, StravaSettingsIntent, StravaSettingsEvent>(
    StravaSettingsState.initial,
    dispatchers
) {
    init {
        preferencesRepository
            .getBooleanPreferenceAsFlow(LOGGED_WITH_BEEPADEL.KEY)
            .onEach { newValue ->
                _state.update {
                    it.copy(
                        loggedWithBeePadelEnabled = newValue ?: LOGGED_WITH_BEEPADEL.DEFAULT_VAL
                    )
                }
            }
            .flowOn(dispatchers.io)
            .launchIn(viewModelScope)
    }

    override fun reduce(intent: StravaSettingsIntent) {
        when (intent) {
            StravaSettingsIntent.LogoutFromStrava -> logoutFromStrava()
            StravaSettingsIntent.NavigateBack -> sendEvent(StravaSettingsEvent.NavigateBack)
            StravaSettingsIntent.ToggleLoggedWithBeePadel ->  ioLaunch {
                preferencesRepository.storeBooleanPreference(
                    LOGGED_WITH_BEEPADEL.KEY, !state.value.loggedWithBeePadelEnabled
                )
            }
        }
    }

    private fun logoutFromStrava() {
        ioLaunch {
            when (val logout = authRepository.logoutFromStrava()) {
                is Result.Success -> {
                    sendEvent(StravaSettingsEvent.NavigateBack)
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(error = logout.error.asUiText())
                    }
                }
            }
        }
    }
}

