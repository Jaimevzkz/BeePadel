package com.vzkz.match.data.connectivity

import com.vzkz.core.connectivity.domain.NodeDiscovery
import com.vzkz.core.connectivity.domain.messaging.MessagingAction
import com.vzkz.core.connectivity.domain.messaging.MessagingClient
import com.vzkz.core.connectivity.domain.model.DeviceNode
import com.vzkz.core.connectivity.domain.model.DeviceType
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.core.domain.error.MessagingError
import com.vzkz.match.domain.WatchConnector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn

@OptIn(ExperimentalCoroutinesApi::class)
class PhoneToWatchConnector(
    nodeDiscovery: NodeDiscovery,
    applicationScope: CoroutineScope,
    private val messagingClient: MessagingClient
) : WatchConnector {
    private val _connectedNode = MutableStateFlow<DeviceNode?>(null)
    override val connectedDevice: StateFlow<DeviceNode?>
        get() = _connectedNode.asStateFlow()

    override val messagingActions: Flow<MessagingAction> =
        nodeDiscovery
            .observeConnectedDevices(DeviceType.PHONE)
            .flatMapLatest { connectedDevices ->
                val node = connectedDevices.firstOrNull()
                if (node != null && node.isNearby) {
                    _connectedNode.value = node
                    messagingClient.connectToNode(node.id)
                } else flowOf()
            }
            .shareIn(
                applicationScope,
                SharingStarted.Eagerly
            )

    init {
        _connectedNode
            .filterNotNull()
            .onEach {
                sendActionToWatch(MessagingAction.ConnectionRequest)
            }
            .launchIn(applicationScope)
    }

    override suspend fun sendActionToWatch(action: MessagingAction): EmptyResult<MessagingError> {
        return messagingClient.sendOrQueueAction(action)
    }

}