package com.vzkz.match.data.util

import com.vzkz.common.general.data_generator.emptyGame
import com.vzkz.common.general.data_generator.emptyMatch
import com.vzkz.match.data.MatchTrackerImpl
import com.vzkz.match.domain.model.Match
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration

internal suspend fun addGame(
    matchTrackerImpl: MatchTrackerImpl,
    team1: Boolean,
    onAwait: suspend () -> Match
): Match {
    val addPoint =
        if (team1) {
            { matchTrackerImpl.addPointToPlayer1() }
        } else {
            { matchTrackerImpl.addPointToPlayer2() }
        }

    (0..<3).forEach { _ ->
        addPoint()
        onAwait()
    }
    addPoint()
    return onAwait()
}

internal suspend fun addSet( // Default to 6-0
    matchTrackerImpl: MatchTrackerImpl,
    onAwait: suspend () -> Match,
    desiredScore: Pair<Int,Int> = (6 to 0)
): Match {
    var emission = emptyMatch()

    val lowerScore = min(desiredScore.first, desiredScore.second)
    val higherScore = max(desiredScore.first, desiredScore.second)

    var team1 = desiredScore.first < desiredScore.second


    (0..<lowerScore).forEach{ _ ->
        emission = addGame(
            matchTrackerImpl = matchTrackerImpl,
            team1 = team1,
            onAwait = onAwait
        )
    }
    team1 = !team1
    (0..<higherScore).forEach { _ ->
        emission = addGame(
            matchTrackerImpl = matchTrackerImpl,
            team1 = team1,
            onAwait = onAwait
        )
    }
    return emission
}