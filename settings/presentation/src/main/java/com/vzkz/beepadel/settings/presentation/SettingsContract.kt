package com.vzkz.beepadel.settings.presentation

import com.vzkz.core.presentation.ui.UiText
import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State

data class SettingsState(
    val error: UiText?,

) : State {
    companion object {
        val initial: SettingsState = SettingsState(
            error = null,

        )
    }
}

sealed class SettingsIntent : Intent {

}

sealed class SettingsEvent : Event {

}
