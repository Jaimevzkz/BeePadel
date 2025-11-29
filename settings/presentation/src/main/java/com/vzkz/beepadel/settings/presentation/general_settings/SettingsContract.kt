package com.vzkz.beepadel.settings.presentation.general_settings

import com.vzkz.common.general.GOLDEN_POINT
import com.vzkz.core.presentation.ui.UiText
import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State

data class SettingsState(
    val error: UiText?,
    val goldenPoint: Boolean,
    val isLoggedIntoStrava: Boolean

) : State {
    companion object {
        val initial: SettingsState = SettingsState(
            error = null,
            goldenPoint = GOLDEN_POINT.DEFAULT_VAL,
            isLoggedIntoStrava = false,
        )
    }
}

sealed class SettingsIntent : Intent {
    data object NavigateBack: SettingsIntent()
    data object NavigateToAbout: SettingsIntent()
    data object ToggleGoldenPoint: SettingsIntent()
    data object LaunchAuthRequestIntent: SettingsIntent()
    data object ConfigureStrava: SettingsIntent()
    data object OpenGithub: SettingsIntent()
    data object OpenPlayStore: SettingsIntent()
    data object ContactUs: SettingsIntent()
}

sealed class SettingsEvent : Event {
    data object NavigateBack: SettingsEvent()
    data object NavigateToAbout: SettingsEvent()
    data class LaunchAuthRequestIntent(val intent: android.content.Intent): SettingsEvent()
    data object ConfigureStrava: SettingsEvent()
    data object OpenGithub: SettingsEvent()
    data object OpenPlayStore: SettingsEvent()
    data object ContactUs: SettingsEvent()
}
