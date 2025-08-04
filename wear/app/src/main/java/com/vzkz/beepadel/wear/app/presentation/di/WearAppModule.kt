package com.vzkz.beepadel.wear.app.presentation.di

import com.vzkz.beepadel.wear.app.presentation.WearBeePadelApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val wearAppModule = module {
    single<CoroutineScope> {
        (androidApplication() as WearBeePadelApp).applicationScope
    }
}