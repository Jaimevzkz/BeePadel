package com.vzkz.beepadel.settings.presentation.strava_settings

import com.vzkz.common.general.LOGGED_WITH_BEEPADEL
import com.vzkz.core.presentation.ui.UiText
import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State

data class StravaSettingsState(
    val error: UiText?,
    val loggedWithBeePadelEnabled: Boolean
) : State {
    companion object {
        val initial: StravaSettingsState = StravaSettingsState(
            error = null,
            loggedWithBeePadelEnabled = LOGGED_WITH_BEEPADEL.DEFAULT_VAL,
        )
    }
}

sealed class StravaSettingsIntent : Intent {
    data object LogoutFromStrava: StravaSettingsIntent()
    data object NavigateBack: StravaSettingsIntent()
    data object ToggleLoggedWithBeePadel: StravaSettingsIntent()
}

sealed class StravaSettingsEvent : Event {
    data object NavigateBack: StravaSettingsEvent()

}

