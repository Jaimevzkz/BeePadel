package com.vzkz.core.data.di

import com.vzkz.core.data.DefaultZonedDateTimeProvider
import com.vzkz.core.data.DefaultUUIDProvider
import com.vzkz.core.data.StandardDispatchers
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.ZonedDateTimeProvider
import com.vzkz.core.domain.error.UUIDProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    singleOf(::StandardDispatchers).bind<DispatchersProvider>()
    singleOf(::DefaultZonedDateTimeProvider).bind<ZonedDateTimeProvider>()
    singleOf(::DefaultUUIDProvider).bind<UUIDProvider>()
}