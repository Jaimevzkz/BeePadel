package com.vzkz.beepadel.wear.match.domain

import com.vzkz.core.connectivity.domain.messaging.MessagingAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration

@OptIn(ExperimentalCoroutinesApi::class)
class MatchTracker(
    private val watchToPhoneConnector: PhoneConnector,
    private val exerciseTracker: ExerciseTracker,
    applicationScope: CoroutineScope
) {
    private val _heartRate = MutableStateFlow(0)
    val hearRate = _heartRate.asStateFlow()

    private val _hasMatchStarted = MutableStateFlow(false)
    val hasMatchStarted = _hasMatchStarted.asStateFlow()

    val elapsedTime = watchToPhoneConnector
        .messagingActions
        .filterIsInstance<MessagingAction.TimeUpdate>()
        .map { it.elapsedDuration }
        .stateIn(applicationScope, SharingStarted.Lazily, Duration.ZERO)

    init {
        watchToPhoneConnector
            .connectedNode
            .filterNotNull()
            .onEach {
                exerciseTracker.prepareExercise()
            }
            .launchIn(applicationScope)

        hasMatchStarted
            .flatMapLatest { isTracking ->
                if (isTracking) exerciseTracker.heartRate
                else emptyFlow()
            }
            .onEach { hearRate ->
                watchToPhoneConnector.sendActionToPhone(MessagingAction.HeartRateUpdate(hearRate))
                _heartRate.value = hearRate
            }
            .launchIn(applicationScope)
    }


    fun setHasMatchStarted(isTracking: Boolean) {
        _hasMatchStarted.value = isTracking
    }
}
