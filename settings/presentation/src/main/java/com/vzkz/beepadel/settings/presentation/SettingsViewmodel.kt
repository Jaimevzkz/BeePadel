package com.vzkz.beepadel.settings.presentation

import androidx.lifecycle.viewModelScope
import com.vzkz.beepadel.core.preferences.domain.PreferencesRepository
import com.vzkz.common.general.GOLDEN_POINT
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.presentation.ui.BaseViewModel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import timber.log.Timber

class SettingsViewModel(
    private val dispatchers: DispatchersProvider,
    private val preferencesRepository: PreferencesRepository
) : BaseViewModel<SettingsState, SettingsIntent, SettingsEvent>(
    SettingsState.initial,
    dispatchers
) {

    init {
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
        }
    }
}
