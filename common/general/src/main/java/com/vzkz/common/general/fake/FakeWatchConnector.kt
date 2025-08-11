package com.vzkz.common.general.fake

import com.vzkz.core.connectivity.domain.messaging.MessagingAction
import com.vzkz.core.connectivity.domain.model.DeviceNode
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.core.domain.error.MessagingError
import com.vzkz.core.domain.error.Result
import com.vzkz.match.domain.WatchConnector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeWatchConnector: WatchConnector {
    override val connectedDevice: StateFlow<DeviceNode?>
        get() = MutableStateFlow<DeviceNode?>(null)
    override val messagingActions: Flow<MessagingAction>
        get() = emptyFlow<MessagingAction>()

    override suspend fun sendActionToWatch(action: MessagingAction): EmptyResult<MessagingError> {
        return Result.Success(Unit)
    }
}