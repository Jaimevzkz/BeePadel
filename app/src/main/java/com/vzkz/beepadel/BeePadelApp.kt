package com.vzkz.beepadel

import android.app.Application
import com.vzkz.beepadel.di.appModules
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class BeePadelApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }


        startKoin {
            androidLogger()
            androidContext(this@BeePadelApp)
            modules(appModules)
        }
    }

}