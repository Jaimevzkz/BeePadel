package com.vzkz.match.domain

import com.vzkz.core.domain.Timer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.time.Duration

class MatchTracker(
    private val applicationScope: CoroutineScope,
) {
    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    val elapsedTime = _elapsedTime.asStateFlow()

    init {
        Timer
            .timeAndEmit()
            .onEach { timer ->
                _elapsedTime.value += timer
            }
            .launchIn(applicationScope)
    }

}