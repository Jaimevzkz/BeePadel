package com.vzkz.core.domain

import java.time.ZonedDateTime

interface ZonedDateTimeProvider {
    fun now(): ZonedDateTime
}
