package com.vzkz.match.domain

import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.model.Match
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface MatchTracker {
    val activeMatch: Flow<Match>
    val elapsedTime: Flow<Duration>

    suspend fun finishMatch(): Result<Unit, DataError.Local>
    fun addPointToPlayer1()
    fun addPointToPlayer2()
    fun undoPoint()
    fun discardMatch()

}