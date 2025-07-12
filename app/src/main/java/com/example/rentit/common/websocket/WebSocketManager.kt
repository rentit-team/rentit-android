package com.example.rentit.common.websocket

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.rentit.data.chat.dto.MessageRequestDto
import com.example.rentit.data.chat.dto.MessageResponseDto
import com.example.rentit.feature.chat.chatroom.model.ChatMessageUiModel
import com.google.gson.Gson
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader

object WebSocketManager {
    private var stompClient: StompClient? = null
    private const val TAG = "WebSocket"
    private var topicDisposable: Disposable? = null
    private var stompLifecycleDisposable: Disposable? = null
    private var sendDisposable: Disposable? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun connect(chatroomId: String, receiverId: Long, token: String, onConnect: () -> Unit, onMessageReceived: (ChatMessageUiModel) -> Unit) {
        val url = "ws://api.rentit.o-r.kr:8080/ws/chat/websocket"
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url)

        stompLifecycleDisposable = stompClient?.lifecycle()?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { lifecycleEvent ->
                when (lifecycleEvent.type) {
                    LifecycleEvent.Type.OPENED -> {
                        Log.d(TAG, "Stomp connection opened")
                        onConnect()
                    }
                    LifecycleEvent.Type.CLOSED -> Log.d(TAG, "Stomp connection closed")
                    LifecycleEvent.Type.ERROR -> Log.d(TAG, "Stomp error", lifecycleEvent.exception)
                    else -> {}
                }
            }

        val header = listOf(StompHeader("Authorization", "Bearer $token"))
        stompClient?.connect(header)

        topicDisposable = stompClient?.topic("/topic/chatroom.$chatroomId")
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { topicMessage ->
                val json = topicMessage.payload
                val dto = Gson().fromJson(json, MessageResponseDto::class.java)
                Log.d(TAG, "Received message: ${topicMessage.payload}")
                onMessageReceived(ChatMessageUiModel(dto.senderId == receiverId, dto.content, dto.sentAt))
            }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(chatroomId: String, senderId: Long, message: String) {
        val dto = MessageRequestDto(senderId, message)
        val json = Gson().toJson(dto)

        sendDisposable = stompClient?.send("/app/chatroom.$chatroomId", json)
            ?.subscribe({
                Log.d(TAG, "Message sent")
            }, { throwable ->
                Log.e(TAG, "Error sending message", throwable)
            })
    }

    fun disconnect() {
        stompLifecycleDisposable?.dispose()
        topicDisposable?.dispose()
        sendDisposable?.dispose()
        stompClient?.disconnect()
        stompClient = null
        Log.d(TAG, "Disconnected from WebSocket")
    }
}