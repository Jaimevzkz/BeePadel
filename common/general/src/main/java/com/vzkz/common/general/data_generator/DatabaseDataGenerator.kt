package com.vzkz.common.general.data_generator

import com.vzkz.core.database.domain.model.GameEntityModel
import com.vzkz.match.domain.model.Points
import java.util.UUID


fun gameEntityModel() = GameEntityModel(
    gameId = UUID.randomUUID(),
    setId = UUID.randomUUID(),
    serverPoints = Points.Won,
    receiverPoints = Points.Zero
)
