package com.vzkz.connectivity.core.data

import com.google.android.gms.wearable.Node
import com.vzkz.core.connectivity.domain.model.DeviceNode

fun Node.toDeviceNode(): DeviceNode {
    return DeviceNode(
        id = id,
        displayName = displayName,
        isNearby = isNearby
    )
}