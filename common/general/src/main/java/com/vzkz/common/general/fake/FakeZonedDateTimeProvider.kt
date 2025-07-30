package com.vzkz.common.general.fake

import com.vzkz.core.domain.ZonedDateTimeProvider
import java.time.ZonedDateTime

class FakeZonedDateTimeProvider(private val fixedTime: ZonedDateTime) : ZonedDateTimeProvider {
    override fun now(): ZonedDateTime = fixedTime
}
