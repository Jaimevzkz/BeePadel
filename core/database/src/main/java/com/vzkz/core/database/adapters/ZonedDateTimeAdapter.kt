package com.vzkz.core.database.adapters

import app.cash.sqldelight.ColumnAdapter
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

val zonedDateTimeAdapter = object : ColumnAdapter<ZonedDateTime, String> {
    override fun decode(databaseValue: String): ZonedDateTime {
        return ZonedDateTime.parse(databaseValue, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    }

    override fun encode(value: ZonedDateTime): String {
        return value.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    }
}
