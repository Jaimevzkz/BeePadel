package com.vzkz.core.database.data.adapters

import app.cash.sqldelight.ColumnAdapter
import java.util.UUID

val uuidAdapter = object : ColumnAdapter<UUID, String> {
    override fun decode(databaseValue: String): UUID {
        return UUID.fromString(databaseValue)
    }

    override fun encode(value: UUID): String {
        return value.toString()
    }
}
