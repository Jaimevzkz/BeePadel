package com.vzkz.match.presentation.di

import com.vzkz.match.domain.MatchTracker
import com.vzkz.match.presentation.active_match.ActiveMatchViewmodel
import com.vzkz.match.presentation.match_history.MatchHistoryViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val matchViewmodelModule = module {
    single{
        get<MatchTracker>().elapsedTime
    }
    viewModelOf(::ActiveMatchViewmodel)
    viewModelOf(::MatchHistoryViewModel)
}