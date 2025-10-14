package com.vzkz.beepadel.settings.presentation.strava_settings

import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.auth.AuthRepository
import com.vzkz.core.domain.error.Result
import com.vzkz.core.presentation.ui.BaseViewModel
import com.vzkz.core.presentation.ui.asUiText
import kotlinx.coroutines.flow.update

class StravaSettingsViewmodel(
    private val dispatchers: DispatchersProvider,
    private val authRepository: AuthRepository
) : BaseViewModel<StravaSettingsState, StravaSettingsIntent, StravaSettingsEvent>(
    StravaSettingsState.initial,
    dispatchers
) {
    override fun reduce(intent: StravaSettingsIntent) {
        when (intent) {
            StravaSettingsIntent.LogoutFromStrava -> logoutFromStrava()
            StravaSettingsIntent.NavigateBack -> sendEvent(StravaSettingsEvent.NavigateBack)
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

