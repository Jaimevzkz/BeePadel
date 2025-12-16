package com.vzkz.settings.data.di

import com.vzkz.settings.data.SettingsRepositoryImpl
import com.vzkz.settings.domain.SettingsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val settingsDataModule = module {
    singleOf(::SettingsRepositoryImpl).bind<SettingsRepository>()

}
