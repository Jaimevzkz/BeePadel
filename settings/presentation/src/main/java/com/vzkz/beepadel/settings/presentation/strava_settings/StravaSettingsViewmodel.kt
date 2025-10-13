package com.vzkz.beepadel.settings.presentation.strava_settings

import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.presentation.ui.BaseViewModel

class StravaSettingsViewmodel(
    private val dispatchers: DispatchersProvider,
) : BaseViewModel<StravaSettingsState, StravaSettingsIntent, StravaSettingsEvent>(
    StravaSettingsState.initial,
    dispatchers
) {
    override fun reduce(intent: StravaSettingsIntent) {
        when (intent) {
            else -> {}
        }
    }
}

