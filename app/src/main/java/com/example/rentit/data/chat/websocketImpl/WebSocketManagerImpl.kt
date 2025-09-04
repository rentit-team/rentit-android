package com.example.rentit.data.chat.websocketImpl

import android.util.Log
import com.example.rentit.data.chat.dto.MessageRequestDto
import com.example.rentit.data.chat.dto.MessageResponseDto
import com.example.rentit.domain.chat.model.ChatMessageModel
import com.example.rentit.domain.chat.websocket.WebSocketManager
import com.example.rentit.domain.user.repository.UserRepository
import com.google.gson.Gson
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import javax.inject.Inject

private const val TAG = "WebSocket"
private const val URL = "ws://api.rentit.o-r.kr:8080/ws/chat/websocket"

class WebSocketManagerImpl @Inject constructor(
    private val userRepository: UserRepository,
): WebSocketManager {
    private var stompClient: StompClient? = null
    private var topicDisposable: Disposable? = null
    private var stompLifecycleDisposable: Disposable? = null
    private var sendDisposable: Disposable? = null

    private fun getToken() = userRepository.getTokenFromPrefs()
    private fun getAuthUserId() = userRepository.getAuthUserIdFromPrefs()

    private fun clearDisposable() {
        stompLifecycleDisposable?.dispose()
        topicDisposable?.dispose()
        sendDisposable?.dispose()
    }

    private fun subscribeLifeCycle(onConnect: () -> Unit) {
        stompLifecycleDisposable = stompClient?.lifecycle()?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> {
                        Log.i(TAG, "Stomp connection opened")
                        onConnect()
                    }
                    LifecycleEvent.Type.CLOSED -> Log.d(TAG, "Stomp connection closed")
                    LifecycleEvent.Type.ERROR -> Log.d(TAG, "Stomp error", lifecycleEvent.exception)
                    else -> { }
                }
            }
    }

    private fun subscribeTopic(chatroomId: String, onMessageReceived: (ChatMessageModel) -> Unit) {
        val authUserId = getAuthUserId()
        topicDisposable = stompClient?.topic("/topic/chatroom.$chatroomId")
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { topicMessage ->
                val json = topicMessage.payload
                val response = Gson().fromJson(json, MessageResponseDto::class.java)
                val chatMessageModel = ChatMessageModel(
                    response.messageId,
                    response.senderId == authUserId,
                    response.content,
                    response.sentAt
                )
                onMessageReceived(chatMessageModel)
                Log.d(TAG, "Received message: $json")
            }
    }

    override fun connect(chatroomId: String, onConnect: () -> Unit, onMessageReceived: (ChatMessageModel) -> Unit) {
        val token = getToken()

        clearDisposable()
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, URL)

        subscribeLifeCycle(onConnect)
        stompClient?.connect(listOf(StompHeader("Authorization", "Bearer $token")))
        subscribeTopic(chatroomId, onMessageReceived)
    }

    override fun sendMessage(chatroomId: String, message: String) {
        val authUserId = getAuthUserId()
        val dto = MessageRequestDto(chatroomId, authUserId, message)
        val json = Gson().toJson(dto)

        sendDisposable = stompClient?.send("/app/chatroom.$chatroomId", json)
            ?.subscribe({
                Log.d(TAG, "Message sent")
            }, { throwable ->
                Log.e(TAG, "Error sending message", throwable)
            })
    }

    override fun disconnect() {
        clearDisposable()
        stompClient?.disconnect()
        stompClient = null
        Log.d(TAG, "Disconnected from WebSocket")
    }
}