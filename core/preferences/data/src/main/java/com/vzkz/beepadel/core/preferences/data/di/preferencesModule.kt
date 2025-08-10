package com.vzkz.beepadel.core.preferences.data.di

import com.vzkz.beepadel.core.preferences.data.DataStoreRepository
import com.vzkz.beepadel.core.preferences.domain.PreferencesRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val preferencesModule = module {
    singleOf(::DataStoreRepository).bind<PreferencesRepository>()
}