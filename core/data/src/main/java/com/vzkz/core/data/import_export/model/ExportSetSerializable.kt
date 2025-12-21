package com.vzkz.core.data.import_export.model

import com.vzkz.core.domain.model.Set
import kotlinx.serialization.Serializable
import java.util.UUID

@kotlinx.serialization.InternalSerializationApi
@Serializable
data class ExportSetSerializable(
    val setId: String,
    val gameList: List<ExportGameSerializable>,
) {
    fun toSet(): Set = Set(
        setId = UUID.fromString(setId),
        gameList = gameList.map { it.toGame() }
    )
}

@kotlinx.serialization.InternalSerializationApi
fun Set.toExportSetSerializable(): ExportSetSerializable = ExportSetSerializable(
    setId = setId.toString(),
    gameList = gameList.map { it.toExportGameSerializable() }
)