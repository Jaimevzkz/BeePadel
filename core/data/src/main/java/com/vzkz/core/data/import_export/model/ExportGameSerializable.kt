package com.vzkz.core.data.import_export.model

import com.vzkz.core.domain.model.Game
import com.vzkz.core.domain.model.toPoints
import kotlinx.serialization.Serializable
import java.util.UUID

@kotlinx.serialization.InternalSerializationApi
@Serializable
data class ExportGameSerializable(
    val gameId: String,
    val player1Points: Int,
    val player2Points: Int
) {
    fun toGame(): Game = Game(
        gameId = UUID.fromString(gameId),
        player1Points = player1Points.toPoints(),
        player2Points = player2Points.toPoints()
    )
}

@kotlinx.serialization.InternalSerializationApi
fun Game.toExportGameSerializable(): ExportGameSerializable = ExportGameSerializable(
    gameId = gameId.toString(),
    player1Points = player1Points.ordinal,
    player2Points = player2Points.ordinal
)
