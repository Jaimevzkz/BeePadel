package com.vzkz.match.presentation.util

import com.vzkz.match.domain.Points
import com.vzkz.match.domain.Set

fun Set.toFormattedSet(): Pair<Int,Int> {
    var gamesPlayer1 = 0
    var gamesPlayer2 = 0
    gameList.forEach { game ->
        if (game.serverPoints == Points.Won) gamesPlayer1++
        else if (game.receiverPoints == Points.Won) gamesPlayer2++
    }

    return Pair(gamesPlayer1, gamesPlayer2)
}