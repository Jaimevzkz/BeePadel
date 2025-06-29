package com.vzkz.match.presentation.util

import android.annotation.SuppressLint
import com.vzkz.match.domain.Match
import com.vzkz.match.domain.Points
import com.vzkz.match.domain.Set
import com.vzkz.match.presentation.match_history.model.MatchUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration

fun Set.toFormattedSet(): Pair<Int, Int> {
    var gamesPlayer1 = 0
    var gamesPlayer2 = 0
    gameList.forEach { game ->
        if (game.serverPoints == Points.Won) gamesPlayer1++
        else if (game.receiverPoints == Points.Won) gamesPlayer2++
    }

    return Pair(gamesPlayer1, gamesPlayer2)
}

fun Duration.formatted(): String {
    val totalSeconds = inWholeSeconds
    val hours = String.format(Locale.ENGLISH, "%02d", totalSeconds / 3600)
    val minutes = String.format(Locale.ENGLISH, "%02d", totalSeconds % 3600 / 60)
    val seconds = String.format(Locale.ENGLISH, "%02d", totalSeconds % 60)

    return "$hours:$minutes:$seconds"
}


fun Match.toMatchUi(): MatchUi {
    val dateTimeInLocalTime = dateTimeUtc
        .withZoneSameInstant(ZoneId.systemDefault())

    val formattedDateTime = DateTimeFormatter
        .ofPattern("MMM dd, yyyy - hh:mma")
        .format(dateTimeInLocalTime)

    val formattedSetList = setList.map { it.toFormattedSet() }
    var setDifference = 0
    formattedSetList.forEach { set ->
        if (set.first > set.second) setDifference++
        else if (set.first < set.second) setDifference--
    }

    return MatchUi(
        isMatchWon = setDifference > 0,
        formatedSetList = formattedSetList,
        dateTimeUtc = formattedDateTime,
        elapsedTime = elapsedTime.formatted(),
    )

}