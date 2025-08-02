package com.vzkz.match.data.di

import com.vzkz.match.data.MatchTrackerImpl
import com.vzkz.match.data.connectivity.PhoneToWatchConnector
import com.vzkz.match.data.match_historrepoimy.MatchHistoryRepositoryImpl
import com.vzkz.match.domain.MatchTracker
import com.vzkz.match.domain.WatchConnector
import com.vzkz.match.domain.match_history.MatchHistoryRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val matchDataModule = module {
    singleOf(::MatchHistoryRepositoryImpl).bind<MatchHistoryRepository>()
    singleOf(::MatchTrackerImpl).bind<MatchTracker>()

    singleOf(::PhoneToWatchConnector).bind<WatchConnector>()
}