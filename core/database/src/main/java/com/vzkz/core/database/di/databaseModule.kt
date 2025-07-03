package com.vzkz.core.database.di

import android.app.Application
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.vzkz.core.database.BeePadelDB
import com.vzkz.core.database.MatchDataSource
import com.vzkz.core.database.MatchDataSourceImpl
import com.vzkz.match.domain.MatchHistoryRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = BeePadelDB.Schema,
            context = get<Application>(),
            name = "beepadel.db"
        )
    }

    singleOf(::MatchDataSourceImpl).bind<MatchDataSource>()
}

