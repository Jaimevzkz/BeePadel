package com.vzkz.common.general.fake

import com.vzkz.core.connectivity.domain.messaging.MessagingAction
import com.vzkz.core.connectivity.domain.model.DeviceNode
import com.vzkz.core.domain.error.DataError
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.core.domain.error.MessagingError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.WatchConnector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeWatchConnector: WatchConnector {

    var errorToReturn: MessagingError? = null

    override val connectedDevice: StateFlow<DeviceNode?>
        get() = MutableStateFlow<DeviceNode?>(null)

    private var _messagingActions = MutableSharedFlow<MessagingAction>()
    override val messagingActions: Flow<MessagingAction>
        get() = _messagingActions.asSharedFlow()

    override suspend fun sendActionToWatch(action: MessagingAction): EmptyResult<MessagingError> {
        if (errorToReturn != null) return Result.Error(errorToReturn!!)

        _messagingActions.emit(action)
        return Result.Success(Unit)
    }
}