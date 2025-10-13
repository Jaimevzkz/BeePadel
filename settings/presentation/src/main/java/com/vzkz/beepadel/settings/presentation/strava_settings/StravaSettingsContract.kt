package com.vzkz.beepadel.settings.presentation.strava_settings

import com.vzkz.core.presentation.ui.UiText
import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State

data class StravaSettingsState(
    val error: UiText?,
) : State {
    companion object {
        val initial: StravaSettingsState = StravaSettingsState(
            error = null,
        )
    }
}

sealed class StravaSettingsIntent : Intent {

}

sealed class StravaSettingsEvent : Event {

}

