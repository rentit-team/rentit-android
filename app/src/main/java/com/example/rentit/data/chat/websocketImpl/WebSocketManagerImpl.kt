package com.example.rentit.data.chat.websocketImpl

import android.util.Log
import com.example.rentit.data.chat.dto.MessageRequestDto
import com.example.rentit.data.chat.dto.MessageResponseDto
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

    private fun subscribeLifeCycle(onError: (Throwable) -> Unit) {
        stompLifecycleDisposable = stompClient?.lifecycle()?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> Log.i(TAG, "Stomp connection opened")
                    LifecycleEvent.Type.CLOSED -> Log.i(TAG, "Stomp connection closed")
                    LifecycleEvent.Type.ERROR -> {
                        Log.e(TAG, "Stomp connection error", lifecycleEvent.exception)
                        onError(lifecycleEvent.exception)
                    }
                    else -> { }
                }
            }
    }

    private fun subscribeTopic(chatroomId: String, onMessageReceived: (MessageResponseDto) -> Unit, onError: (Throwable) -> Unit) {
        topicDisposable = stompClient?.topic("/topic/chatroom.$chatroomId")
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe ({ topicMessage ->
                val json = topicMessage.payload
                val response = Gson().fromJson(json, MessageResponseDto::class.java)
                onMessageReceived(response)
                Log.i(TAG, "Received message: $json")
            }, { e ->
                Log.e(TAG, "Error subscribing topic", e)
                onError(e)
            })
    }

    override fun connect(chatroomId: String, onMessageReceived: (MessageResponseDto) -> Unit, onError: (Throwable) -> Unit) {
        val token = getToken()

        clearDisposable()
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, URL)

        subscribeLifeCycle(onError)
        stompClient?.connect(listOf(StompHeader("Authorization", "Bearer $token")))
        subscribeTopic(chatroomId, onMessageReceived, onError)
    }

    override fun sendMessage(chatroomId: String, message: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        val authUserId = getAuthUserId()
        val dto = MessageRequestDto(chatroomId, authUserId, message)
        val json = Gson().toJson(dto)

        sendDisposable = stompClient?.send("/app/chatroom.$chatroomId", json)
            ?.subscribe({
                Log.i(TAG, "Message sent")
                onSuccess()
            }, { e ->
                onError(e)
            })
    }

    override fun disconnect() {
        clearDisposable()
        stompClient?.disconnect()
        stompClient = null
        Log.i(TAG, "Disconnected from WebSocket")
    }
}