package com.vzkz.beepadel

import android.app.Application
import com.vzkz.core.data.di.coreDataModule
import com.vzkz.core.database.di.databaseModule
import com.vzkz.match.data.di.matchDataModule
import com.vzkz.match.presentation.di.matchViewmodelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class BeePadelApp: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin{
            androidLogger()
            androidContext(this@BeePadelApp)
            modules(
                coreDataModule,
                matchViewmodelModule,
                matchDataModule,
                databaseModule
            )
        }
    }

}