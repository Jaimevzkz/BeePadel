package com.vzkz.match.domain.model

import java.util.UUID

data class Game(
    val gameId: UUID,
    val serverPoints: Points,
    val receiverPoints: Points
)