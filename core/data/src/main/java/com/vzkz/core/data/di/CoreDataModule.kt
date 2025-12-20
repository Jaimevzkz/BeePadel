package com.vzkz.core.data.di

import com.vzkz.core.data.DefaultZonedDateTimeProvider
import com.vzkz.core.data.DefaultUUIDProvider
import com.vzkz.core.data.StandardDispatchers
import com.vzkz.core.data.auth.AppAuthRepositoryImpl
import com.vzkz.core.data.EncryptedSessionStorage
import com.vzkz.core.data.import_export.ImportExportToJson
import com.vzkz.core.data.networking.HttpClientFactory
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.ImportExportRepository
import com.vzkz.core.domain.SessionStorage
import com.vzkz.core.domain.ZonedDateTimeProvider
import com.vzkz.core.domain.auth.AuthRepository
import com.vzkz.core.domain.error.UUIDProvider
import kotlinx.serialization.json.Json
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
    singleOf(::ImportExportToJson).bind<ImportExportRepository>()
    single {
        Json {
            prettyPrint = true
            encodeDefaults = true
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }
}