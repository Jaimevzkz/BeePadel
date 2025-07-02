package com.vzkz.match.domain.model

data class Game(
    val serverId: Int,
    val serverPoints: Points,
    val receiverPoints: Points
)