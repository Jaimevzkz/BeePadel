package com.vzkz.core.database.data.util

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.vzkz.common.general.TestDispatchers
import com.vzkz.common.test.util.MainCoroutineExtension
import com.vzkz.core.database.data.BeePadelDB
import com.vzkz.core.database.data.adapters.durationAdapter
import com.vzkz.core.database.data.adapters.uuidAdapter
import com.vzkz.core.database.data.adapters.zonedDateTimeAdapter
import game.GameEntity
import match.MatchEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.RegisterExtension
import set.SetEntity

abstract class DatabaseTest {
    protected lateinit var testDispatchers: TestDispatchers
    protected lateinit var beePadelDB: BeePadelDB

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    @BeforeEach
    fun setUp() {
        testDispatchers = TestDispatchers(mainCoroutineExtension.testDispatcher)

        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        BeePadelDB.Schema.create(driver)
        beePadelDB =
            BeePadelDB(
                driver = driver,
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
}