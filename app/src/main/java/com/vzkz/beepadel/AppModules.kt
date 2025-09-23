package com.vzkz.beepadel

import com.vzkz.beepadel.core.preferences.data.di.preferencesModule
import com.vzkz.beepadel.di.appModule
import com.vzkz.beepadel.settings.presentation.di.settingsViewmodelModule
import com.vzkz.connectivity.core.data.di.coreConnectivityDataModule
import com.vzkz.core.data.di.coreDataModule
import com.vzkz.core.database.data.di.databaseModule
import com.vzkz.match.data.di.matchDataModule
import com.vzkz.match.presentation.di.matchViewmodelModule

internal val appModules = listOf(
    appModule,
    coreDataModule,
    matchViewmodelModule,
    matchDataModule,
    databaseModule,
    coreConnectivityDataModule,
    settingsViewmodelModule,
    preferencesModule
)