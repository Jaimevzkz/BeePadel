package com.vzkz.core.database.adapters

import app.cash.sqldelight.ColumnAdapter
import kotlin.time.Duration

val durationAdapter = object : ColumnAdapter<Duration, String> {
    override fun decode(databaseValue: String): Duration {
        return Duration.parse(databaseValue)
    }

    override fun encode(value: Duration): String {
        return value.toIsoString()
    }
}
