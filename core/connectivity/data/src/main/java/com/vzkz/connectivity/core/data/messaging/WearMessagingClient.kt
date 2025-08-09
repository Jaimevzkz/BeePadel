package com.vzkz.connectivity.core.data.messaging

import android.content.Context
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import com.vzkz.core.connectivity.domain.messaging.MessagingAction
import com.vzkz.core.connectivity.domain.messaging.MessagingClient
import com.vzkz.core.domain.error.EmptyResult
import com.vzkz.core.domain.error.MessagingError
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import com.vzkz.core.domain.error.Result
import timber.log.Timber

class WearMessagingClient(
    context: Context
) : MessagingClient {
    private val client = Wearable.getMessageClient(context)
    private val messageQueue = mutableListOf<MessagingAction>()
    private var connectedNodeId: String? = null

    override fun connectToNode(nodeId: String): Flow<MessagingAction> {
        connectedNodeId = nodeId
        return callbackFlow {
            val listener: (MessageEvent) -> Unit = { event ->
                Timber.i("Event received in message event listener: $event")
                if (event.path.startsWith(BASE_PATH_MESSAGING_ACTION)) {
                    val json = event.data.decodeToString()
                    Timber.i("decoded message json: $json")
                    val action = Json.decodeFromString<MessagingActionDto>(json)
                    trySend(action.toMessagingAction())
                }
            }
            val onMessageReceivedListener = MessageClient.OnMessageReceivedListener { listener }

//            client.addListener(onMessageReceivedListener)
            client.addListener(listener)

            messageQueue.forEach {
                sendOrQueueAction(it)
            }
            messageQueue.clear()
            awaitClose {
//                client.removeListener(onMessageReceivedListener)
                client.removeListener(listener)
            }
        }
    }

    override suspend fun sendOrQueueAction(action: MessagingAction): EmptyResult<MessagingError> {
        return connectedNodeId?.let { id ->
            try {
                val json = Json.encodeToString(action.toMessagingActionDto())
                Timber.i("Sending json to phone: $json")
                client.sendMessage(id, BASE_PATH_MESSAGING_ACTION, json.encodeToByteArray()).await()

                Result.Success(Unit)
            } catch (e: ApiException) {
                Timber.e("ApiException while sending message: $e")
                Result.Error(
                    if (e.status.isInterrupted) {
                        MessagingError.CONNECTION_INTERRUPTED
                    } else MessagingError.UNKNOWN
                )
            }
        } ?: run {
            messageQueue.add(action)
            Result.Error(MessagingError.DISCONNECTED)
        }
    }

    companion object {
        private const val BASE_PATH_MESSAGING_ACTION = "beepadel/messaging_action"
    }
}