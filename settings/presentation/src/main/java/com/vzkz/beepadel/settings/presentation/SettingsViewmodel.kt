package com.vzkz.beepadel.settings.presentation

import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.presentation.ui.BaseViewModel

class SettingsViewModel(
    private val dispatchers: DispatchersProvider,
) : BaseViewModel<SettingsState, SettingsIntent, SettingsEvent>(
    SettingsState.initial,
    dispatchers
) {
    override fun reduce(intent: SettingsIntent) {
        when (intent) {
            else -> {}
        }
    }
}
