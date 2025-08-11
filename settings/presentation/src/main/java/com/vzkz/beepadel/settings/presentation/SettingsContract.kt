package com.vzkz.beepadel.settings.presentation

import com.vzkz.common.general.GOLDEN_POINT
import com.vzkz.core.presentation.ui.UiText
import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State

data class SettingsState(
    val error: UiText?,
    val goldenPoint: Boolean,

) : State {
    companion object {
        val initial: SettingsState = SettingsState(
            error = null,
            goldenPoint = GOLDEN_POINT.DEFAULT_VAL,
        )
    }
}

sealed class SettingsIntent : Intent {
    data object NavigateBack: SettingsIntent()
    data object ToggleGoldenPoint: SettingsIntent()
}

sealed class SettingsEvent : Event {
    data object NavigateBack: SettingsEvent()
}
