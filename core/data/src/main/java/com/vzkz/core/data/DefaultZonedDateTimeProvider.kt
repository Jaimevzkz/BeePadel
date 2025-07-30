package com.vzkz.core.data

import com.vzkz.core.domain.ZonedDateTimeProvider
import java.time.ZonedDateTime

class DefaultZonedDateTimeProvider(
) : ZonedDateTimeProvider {
    override fun now(): ZonedDateTime = ZonedDateTime.now()
}
