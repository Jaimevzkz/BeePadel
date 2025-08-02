package com.vzkz.connectivity.core.data.di

import com.vzkz.connectivity.core.data.WearNodeDiscovery
import com.vzkz.connectivity.core.data.messaging.WearMessagingClient
import com.vzkz.core.connectivity.domain.NodeDiscovery
import com.vzkz.core.connectivity.domain.messaging.MessagingClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreConnectivityDataModule = module {
    singleOf(::WearNodeDiscovery).bind<NodeDiscovery>()
    singleOf(::WearMessagingClient).bind<MessagingClient>()
}