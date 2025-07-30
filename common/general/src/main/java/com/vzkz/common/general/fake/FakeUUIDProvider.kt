package com.vzkz.common.general.fake

import com.vzkz.common.general.data_generator.fixedZonedDateTime
import com.vzkz.core.domain.error.UUIDProvider
import java.util.UUID

class FakeUUIDProvider(private val fixedUUID: UUID): UUIDProvider {
    override fun randomUUID(): UUID {
        return fixedUUID
    }
}