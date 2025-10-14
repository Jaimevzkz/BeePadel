package com.vzkz.beepadel.settings.presentation.di

import com.vzkz.beepadel.settings.presentation.general_settings.SettingsViewModel
import com.vzkz.beepadel.settings.presentation.strava_settings.StravaSettingsViewmodel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsViewmodelModule = module {
    viewModelOf(::SettingsViewModel)
    viewModelOf(::StravaSettingsViewmodel)
}