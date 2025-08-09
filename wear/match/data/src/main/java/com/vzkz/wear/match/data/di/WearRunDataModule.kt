package com.vzkz.wear.match.data.di

import com.vzkz.beepadel.wear.match.domain.ExerciseTracker
import com.vzkz.beepadel.wear.match.domain.MatchTracker
import com.vzkz.beepadel.wear.match.domain.PhoneConnector
import com.vzkz.wear.match.data.HealthServicesExerciseTracker
import com.vzkz.wear.match.data.WatchToPhoneConnector
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val wearRunDataModule = module {
    singleOf(::HealthServicesExerciseTracker).bind<ExerciseTracker>()
    singleOf(::WatchToPhoneConnector).bind<PhoneConnector>()
    singleOf(::MatchTracker)
//    single {
//        get<RunningTracker>().elapsedTime
//    }
}