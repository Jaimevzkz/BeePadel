package com.vzkz.beepadel.di

import com.vzkz.beepadel.BeePadelApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as BeePadelApp).applicationScope
    }
}