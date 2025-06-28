package com.vzkz.core.data.di

import com.vzkz.core.data.BeeLoggerImpl
import com.vzkz.core.data.StandardDispatchers
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.log.BeeLogger
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    singleOf(::BeeLoggerImpl).bind<BeeLogger>()
    singleOf(::StandardDispatchers).bind<DispatchersProvider>()
}