package com.vzkz.core.domain.error

import java.util.UUID

interface UUIDProvider {
    fun randomUUID(): UUID
}