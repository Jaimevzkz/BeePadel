package com.vzkz.core.data.di

import com.vzkz.core.data.DefaultZonedDateTimeProvider
import com.vzkz.core.data.DefaultUUIDProvider
import com.vzkz.core.data.StandardDispatchers
import com.vzkz.core.data.AppAuthRepositoryImpl
import com.vzkz.core.data.EncryptedSessionStorage
import com.vzkz.core.data.networking.HttpClientFactory
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.SessionStorage
import com.vzkz.core.domain.ZonedDateTimeProvider
import com.vzkz.core.domain.auth.AuthRepository
import com.vzkz.core.domain.error.UUIDProvider
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    singleOf(::StandardDispatchers).bind<DispatchersProvider>()
    singleOf(::DefaultZonedDateTimeProvider).bind<ZonedDateTimeProvider>()
    singleOf(::DefaultUUIDProvider).bind<UUIDProvider>()

    single{
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
    singleOf(::AppAuthRepositoryImpl).bind<AuthRepository>()
}