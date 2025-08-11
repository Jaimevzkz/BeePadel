package com.vzkz.match.domain.model

import java.util.UUID

data class Game(
    val gameId: UUID,
    val player1Points: Points,
    val player2Points: Points
){
    fun addPointTo(player1: Boolean, goldenPoint: Boolean): Game {
        var pointsToChange = if (player1) player1Points else player2Points
        var otherPoints = if (!player1) player1Points else player2Points
        pointsToChange = when (pointsToChange) {
            Points.Zero -> Points.Fifteen
            Points.Fifteen -> Points.Thirty
            Points.Thirty -> Points.Forty
            Points.Forty -> {
                if (goldenPoint || otherPoints.ordinal < Points.Forty.ordinal) Points.Won
                else if (otherPoints == Points.Forty) {
                    Points.Advantage
                } else {
                    otherPoints = Points.Forty
                    Points.Forty
                }
            }

            Points.Advantage -> Points.Won
            Points.Won -> Points.Won
        }
        return this.copy(
            player1Points = if (player1) pointsToChange else otherPoints,
            player2Points = if (!player1) pointsToChange else otherPoints
        )
    }
}

fun List<Game>.getGameCount(): Pair<Int, Int>{
    var gamesPlayer1 = 0
    var gamesPlayer2 = 0
    this.forEach { game ->
        if (game.player1Points == Points.Won) gamesPlayer1++
        else if (game.player2Points == Points.Won) gamesPlayer2++
    }

    return Pair(gamesPlayer1, gamesPlayer2)
}
