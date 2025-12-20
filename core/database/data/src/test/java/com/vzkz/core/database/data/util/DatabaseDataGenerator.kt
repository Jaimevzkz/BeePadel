package com.vzkz.core.database.data.util

import com.vzkz.common.general.data_generator.defaultUUID
import com.vzkz.core.domain.model.Points
import game.GameEntity


fun gameEntity() = GameEntity(
    gameId = defaultUUID(randomize = true),
    setId = defaultUUID(randomize = true),
    serverPoints = Points.Won,
    receiverPoints = Points.Zero
)
