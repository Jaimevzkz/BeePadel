package com.vzkz.core.database.data.di

import android.app.Application
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.vzkz.core.database.data.BeePadelDB
import com.vzkz.core.database.data.GameDataSourceImpl
import com.vzkz.core.database.data.MatchDataSourceImpl
import com.vzkz.core.database.data.SetDataSourceImpl
import com.vzkz.core.database.data.adapters.durationAdapter
import com.vzkz.core.database.data.adapters.uuidAdapter
import com.vzkz.core.database.data.adapters.zonedDateTimeAdapter
import com.vzkz.core.database.domain.GameDataSource
import com.vzkz.core.database.domain.MatchDataSource
import com.vzkz.core.database.domain.SetDataSource
import game.GameEntity
import match.MatchEntity
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import set.SetEntity

val databaseModule = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = BeePadelDB.Schema,
            context = get<Application>(),
            name = "beepadel.db",
            callback = object : AndroidSqliteDriver.Callback(BeePadelDB.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    db.setForeignKeyConstraintsEnabled(true)
                }
            }
        )
    }

    single<BeePadelDB> {
        BeePadelDB(
            driver = get<SqlDriver>(),
            matchEntityAdapter = MatchEntity.Adapter(
                matchIdAdapter = uuidAdapter,
                dateTimeUtcAdapter = zonedDateTimeAdapter,
                elapsedTimeAdapter = durationAdapter
            ),
            setEntityAdapter = SetEntity.Adapter(
                setIdAdapter = uuidAdapter,
                matchIdAdapter = uuidAdapter
            ),
            gameEntityAdapter = GameEntity.Adapter(
                gameIdAdapter = uuidAdapter,
                setIdAdapter = uuidAdapter,
                serverPointsAdapter = EnumColumnAdapter(),
                receiverPointsAdapter = EnumColumnAdapter()
            ),
        )
    }

    singleOf(::MatchDataSourceImpl).bind<MatchDataSource>()
    singleOf(::SetDataSourceImpl).bind<SetDataSource>()
    singleOf(::GameDataSourceImpl).bind<GameDataSource>()

}

