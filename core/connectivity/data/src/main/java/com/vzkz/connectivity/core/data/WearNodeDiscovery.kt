package com.vzkz.connectivity.core.data

import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Wearable
import com.vzkz.core.connectivity.domain.NodeDiscovery
import com.vzkz.core.connectivity.domain.model.DeviceNode
import com.vzkz.core.connectivity.domain.model.DeviceType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class WearNodeDiscovery(
    context: Context
) : NodeDiscovery {
    private val capabilityClient = Wearable.getCapabilityClient(context)

    override fun observeConnectedDevices(localDeviceType: DeviceType): Flow<Set<DeviceNode>> {
        return callbackFlow {
            val remoteCapability = when (localDeviceType) {
                DeviceType.PHONE -> "beepadel_wear_app"
                DeviceType.WATCH -> "beepadel_phone_app"
            }
            try {
                val capability = capabilityClient
                    .getCapability(remoteCapability, CapabilityClient.FILTER_REACHABLE)
                    .await()
                val connectedDevices = capability.nodes.map { it.toDeviceNode() }.toSet()
                send(connectedDevices)
            } catch (e: ApiException) {
                awaitClose()
                return@callbackFlow
            }

            val listener: (CapabilityInfo) -> Unit = {
                trySend(it.nodes.map { node -> node.toDeviceNode() }.toSet())
            }
//            val onCapabilityChangedListener = CapabilityClient.OnCapabilityChangedListener {listener}

            capabilityClient.addListener(listener, remoteCapability)
            awaitClose {
                capabilityClient.removeListener(listener)
            }
        }
    }
}