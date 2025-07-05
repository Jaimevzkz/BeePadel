package com.vzkz.core.database.domain.model

import com.vzkz.match.domain.model.Points
import java.util.UUID

data class GameEntityModel(
    val gameId: UUID,
    val setId: UUID,
    val serverPoints: Points,
    val receiverPoints: Points
)
