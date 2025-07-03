package com.vzkz.match.data.di

import com.vzkz.common.fake.FakeMatchHistoryRepository
import com.vzkz.match.domain.MatchHistoryRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val matchDataModule = module {
    singleOf(::FakeMatchHistoryRepository).bind<MatchHistoryRepository>() // TODO replace fake with real impl
}