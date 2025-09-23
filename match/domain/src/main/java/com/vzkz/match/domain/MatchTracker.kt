package com.vzkz.match.domain

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.swing.plaf.nimbus.State
import kotlin.time.Duration

interface MatchTracker {
    val activeMatch: Flow<Match>
    val elapsedTime: StateFlow<Duration>
    val isTeam1Serving: Flow<Boolean?>
    val goldenPoint: Flow<Boolean>
    val isMatchStarted: Flow<Boolean>
    val currentHeartRate: Flow<Int?>

    fun addPointToPlayer1()
    fun addPointToPlayer2()
    fun undoPoint()
    suspend fun discardMatch()
    suspend fun finishMatch(): Result<Unit, DataError>

    fun setIsTeam1Serving(isTeam1Serving: Boolean?)
    fun setIsMatchStarted(isPlayingMatch: Boolean)

    companion object Constants {
        const val DISCARD_MATCH_DELAY = 50L
    }
}