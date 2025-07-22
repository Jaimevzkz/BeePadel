package com.vzkz.match.domain.model

import java.util.UUID

data class Set(
    val setId: UUID,
    val gameList: List<Game>,
){
    fun getWinner(): Boolean? { // true -> player1
        val formattedSet = this.getGamesForSet()

        return if ((formattedSet.first == 6 && formattedSet.second < 5) || formattedSet.first == 7) true
        else if ((formattedSet.second == 6 && formattedSet.first < 5) || formattedSet.second == 7) false
        else null
    }


    fun getGamesForSet(): Pair<Int, Int> {
        var gamesPlayer1 = 0
        var gamesPlayer2 = 0
        gameList.forEach { game ->
            if (game.player1Points == Points.Won) gamesPlayer1++
            else if (game.player2Points == Points.Won) gamesPlayer2++
        }

        return Pair(gamesPlayer1, gamesPlayer2)
    }

}