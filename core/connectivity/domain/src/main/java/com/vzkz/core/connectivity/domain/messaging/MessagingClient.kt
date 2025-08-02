package com.vzkz.core.connectivity.domain.messaging

import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.core.domain.error.MessagingError
import kotlinx.coroutines.flow.Flow

interface MessagingClient {
    fun connectToNode(nodeId: String): Flow<MessagingAction>
    suspend fun sendOrQueueAction(action: MessagingAction): EmptyResult<MessagingError>

}