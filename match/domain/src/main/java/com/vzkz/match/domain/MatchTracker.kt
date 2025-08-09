package com.vzkz.match.domain

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface MatchTracker {
    val activeMatch: Flow<Match>
    val elapsedTime: Flow<Duration>
    val isTeam1Serving: Flow<Boolean?>
    val isMatchStarted: Flow<Boolean>
    val isMatchResumed: Flow<Boolean>
    val currentHeartRate: Flow<Int?>

    fun addPointToPlayer1()
    fun addPointToPlayer2()
    fun undoPoint()
    suspend fun discardMatch()
    suspend fun finishMatch(): Result<Unit, DataError>

    fun setIsTeam1Serving(isTeam1Serving: Boolean?)
    fun setPlayingMatch(isPlayingMatch: Boolean)
    fun setIsMatchStarted(isMatchStarted: Boolean)
}