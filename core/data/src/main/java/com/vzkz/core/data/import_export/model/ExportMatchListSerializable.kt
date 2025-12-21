package com.vzkz.core.data.import_export.model

import kotlinx.serialization.Serializable

@kotlinx.serialization.InternalSerializationApi
@Serializable
data class ExportMatchListSerializable(
    val exportedAt: Long,
    val items: List<ExportMatchSerializable>
)


