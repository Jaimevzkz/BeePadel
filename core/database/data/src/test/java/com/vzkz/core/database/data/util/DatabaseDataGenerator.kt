package com.vzkz.core.database.data.util

import com.vzkz.match.domain.model.Points
import game.GameEntity
import java.util.UUID


fun gameEntity() = GameEntity(
    gameId = UUID.randomUUID(),
    setId = UUID.randomUUID(),
    serverPoints = Points.Won,
    receiverPoints = Points.Zero
)
