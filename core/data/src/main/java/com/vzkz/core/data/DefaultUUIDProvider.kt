package com.vzkz.core.data

import com.vzkz.core.domain.error.UUIDProvider
import java.util.UUID

class DefaultUUIDProvider: UUIDProvider {
    override fun randomUUID(): UUID = UUID.randomUUID()
}