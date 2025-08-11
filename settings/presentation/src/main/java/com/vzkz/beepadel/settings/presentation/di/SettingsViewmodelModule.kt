package com.vzkz.beepadel.settings.presentation.di

import com.vzkz.beepadel.settings.presentation.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsViewmodelModule = module {
    viewModelOf(::SettingsViewModel)
}