package com.vzkz.match.domain

data class Game(
    val serverId: Int,
    val serverPoints: Points,
    val receiverPoints: Points
)