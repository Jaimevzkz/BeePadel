package com.vzkz.beepadel.wear.presentation.di

import com.vzkz.beepadel.wear.presentation.active_match.WearActiveMatchViewmodel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val wearMatchViewmodelModule = module {
    viewModelOf(::WearActiveMatchViewmodel)
}