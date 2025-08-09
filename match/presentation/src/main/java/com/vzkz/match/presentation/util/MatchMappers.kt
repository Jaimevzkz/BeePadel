package com.vzkz.match.presentation.util

import com.vzkz.match.domain.model.Match
import com.vzkz.match.presentation.match_history.model.MatchUi
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.max
import kotlin.time.Duration

fun Duration.formatted(): String {
    val totalSeconds = inWholeSeconds
    val hours = String.format(Locale.ENGLISH, "%02d", totalSeconds / 3600)
    val minutes = String.format(Locale.ENGLISH, "%02d", totalSeconds % 3600 / 60)
    val seconds = String.format(Locale.ENGLISH, "%02d", totalSeconds % 60)

    return "$hours:$minutes:$seconds"
}


fun Match.toMatchUi(zoneId: ZoneId = ZoneId.systemDefault()): MatchUi {
    val dateTimeInLocalTime = dateTime
        .withZoneSameInstant(zoneId)

    val formattedDateTime = DateTimeFormatter
        .ofPattern("MMM dd, yyyy - hh:mma")
        .format(dateTimeInLocalTime)

    val formattedSetList = setList.map { it.getGamesForSet() }
    var setDifference = 0
    formattedSetList.forEach { set ->
        if (set.first > set.second) setDifference++
        else if (set.first < set.second) setDifference--
    }

    return MatchUi(
        matchId = matchId,
        isMatchWon = setDifference > 0,
        formatedSetList = formattedSetList,
        dateTimeFormatted = formattedDateTime,
        elapsedTime = elapsedTime.formatted(),
        avgHeartRate = avgHeartRate,
        maxHeartRate = maxHeartRate,
    )

}