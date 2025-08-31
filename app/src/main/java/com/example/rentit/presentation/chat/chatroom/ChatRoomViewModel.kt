package com.example.rentit.presentation.chat.chatroom

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.websocket.WebSocketManager
import com.example.rentit.domain.chat.usecase.GetChatRoomDetailUseCase
import com.example.rentit.domain.user.repository.UserRepository
import com.example.rentit.presentation.chat.chatroom.model.ChatMessageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getChatRoomDetailUseCase: GetChatRoomDetailUseCase
): ViewModel() {

    private val authUserId: Long = userRepository.getAuthUserIdFromPrefs()

    private val _realTimeMessages = MutableStateFlow<List<ChatMessageUiModel>>(emptyList())
    val realTimeMessages: StateFlow<List<ChatMessageUiModel>> = _realTimeMessages

    @RequiresApi(Build.VERSION_CODES.O)
    fun getChatDetail(chatRoomId: String, onError: (Throwable) -> Unit = {}) {
        viewModelScope.launch {
            getChatRoomDetailUseCase(chatRoomId)
                .onSuccess { }
                .onFailure(onError)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun connectWebSocket(chatRoomId: String, onConnect: () -> Unit) {
        val token = userRepository.getTokenFromPrefs() ?: return
        WebSocketManager.connect(chatRoomId, token, onConnect) { data ->
            val msg = ChatMessageUiModel(data.senderId == authUserId, data.content, data.sentAt)
            _realTimeMessages.value = listOf(msg) + _realTimeMessages.value
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(chatRoomId: String, message: String) {
        WebSocketManager.sendMessage(chatRoomId, authUserId, message)
    }

    fun disconnectWebSocket() {
        WebSocketManager.disconnect()
    }

    fun resetRealTimeMessages() {
        _realTimeMessages.value = emptyList()
    }
}