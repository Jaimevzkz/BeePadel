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
        return gameList.getGameCount()
    }

}

fun List<Set>.getSetCount(): Pair<Int, Int>{
    var setsPlayer1 = 0
    var setsPlayer2 = 0
    this.forEach { set ->
        if (set.getWinner() == true) setsPlayer1++
        else if (set.getWinner() == false) setsPlayer2++
    }

    return Pair(setsPlayer1, setsPlayer2)
}
