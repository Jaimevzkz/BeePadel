package com.vzkz.core.presentation.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vzkz.core.domain.DispatchersProvider
import com.vzkz.core.domain.log.BeeLogger
import com.vzkz.core.domain.log.LogType
import com.vzkz.core.presentation.ui.model.Event
import com.vzkz.core.presentation.ui.model.Intent
import com.vzkz.core.presentation.ui.model.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val BUFFER_SIZE = 64

abstract class BaseViewModel<S : State, I : Intent, E : Event>(
    initialState: S,
    private val dispatchers: DispatchersProvider,
    private val beeLogger: BeeLogger
) : ViewModel() {

    private val intents =
        MutableSharedFlow<I>(extraBufferCapacity = BUFFER_SIZE) // Intent pipeline

    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> get() = _state.asStateFlow()

    private val _events = Channel<E?>(Channel.BUFFERED)
    val events: Flow<E?> = _events.receiveAsFlow()

    init {
        viewModelScope.launch(dispatchers.default) {
            intents.collect { intent -> //For each action in the pipeline we execute the reduce method to update the state
                reduce(intent)
            }
        }
    }

    protected fun ioLaunch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(dispatchers.io) { block() }
    }

    protected abstract fun reduce(intent: I)

    protected fun sendEvent(event: E) {
        val success = _events.trySend(event)
        if (success.isFailure)
            beeLogger.log("Send event failed", logType = LogType.ERROR)
    }

    fun onAction(intent: I) {
        val success = intents.tryEmit(intent)
        if (!success)
            beeLogger.log("MVI action buffer overflow on intent: $intent", logType = LogType.ERROR)
    }
}