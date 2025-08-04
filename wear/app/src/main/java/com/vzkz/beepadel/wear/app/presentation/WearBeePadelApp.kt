package com.vzkz.beepadel.wear.app.presentation

import android.app.Application
import com.vzkz.beepadel.wear.app.BuildConfig
import com.vzkz.beepadel.wear.app.presentation.di.wearAppModule
import com.vzkz.beepadel.wear.presentation.di.wearMatchViewmodelModule
import com.vzkz.connectivity.core.data.di.coreConnectivityDataModule
import com.vzkz.core.data.di.coreDataModule
import com.vzkz.wear.match.data.di.wearRunDataModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class WearBeePadelApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@WearBeePadelApp)
            modules(
                wearAppModule,
                wearMatchViewmodelModule,
                wearRunDataModule,
                coreDataModule,
                coreConnectivityDataModule,
            )
        }
    }

}