package com.vzkz.core.data.import_export.model

import kotlinx.serialization.Serializable

@Serializable
data class ExportMatchListSerializable(
    val exportedAt: Long,
    val items: List<ExportMatchSerializable>
)


