package com.vzkz.core.connectivity.domain

import com.vzkz.core.connectivity.domain.model.DeviceNode
import com.vzkz.core.connectivity.domain.model.DeviceType
import kotlinx.coroutines.flow.Flow

interface NodeDiscovery {
    fun observeConnectedDevices(localDeviceType: DeviceType): Flow<Set<DeviceNode>>
}