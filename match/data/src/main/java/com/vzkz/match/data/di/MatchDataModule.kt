package com.vzkz.match.data.di

import com.vzkz.match.active_match.ActiveMatchRepositoryImpl
import com.vzkz.match.domain.active_match.ActiveMatchRepository
import com.vzkz.match.match_history.MatchHistoryRepositoryImpl
import com.vzkz.match.domain.match_history.MatchHistoryRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val matchDataModule = module {
    singleOf(::MatchHistoryRepositoryImpl).bind<MatchHistoryRepository>()
    singleOf(::ActiveMatchRepositoryImpl).bind<ActiveMatchRepository>()
}