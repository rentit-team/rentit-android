package com.example.rentit.presentation.chat.chatroom

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.rentit.common.websocket.WebSocketManager
import com.example.rentit.domain.chat.model.ChatMessageModel
import com.example.rentit.domain.chat.usecase.GetChatRoomDetailUseCase
import com.example.rentit.domain.product.usecase.GetChatRoomProductSummaryUseCase
import com.example.rentit.domain.rental.usecase.GetChatRoomRentalSummaryUseCase
import com.example.rentit.domain.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getChatRoomRentalSummaryUseCase: GetChatRoomRentalSummaryUseCase,
    private val getChatRoomProductSummaryUseCase: GetChatRoomProductSummaryUseCase,
    private val getChatRoomDetailUseCase: GetChatRoomDetailUseCase
): ViewModel() {

    private val authUserId: Long = userRepository.getAuthUserIdFromPrefs()

    private val _realTimeMessages = MutableStateFlow<List<ChatMessageModel>>(emptyList())
    val realTimeMessages: StateFlow<List<ChatMessageModel>> = _realTimeMessages

    private suspend fun fetchChatDetail(chatRoomId: String) {
        getChatRoomDetailUseCase(chatRoomId)
            .onSuccess { println(it) }
            .onFailure { }
    }

    private suspend fun fetchRentalSummary(productId: Int, reservationId: Int) {
        getChatRoomRentalSummaryUseCase(productId, reservationId)
            .onSuccess { println(it) }
            .onFailure { }
    }

    private suspend fun fetchProductSummary(productId: Int) {
        getChatRoomProductSummaryUseCase(productId)
            .onSuccess { println(it) }
            .onFailure { }
    }

    fun connectWebSocket(chatRoomId: String, onConnect: () -> Unit) {
        val token = userRepository.getTokenFromPrefs() ?: return
        WebSocketManager.connect(chatRoomId, token, onConnect) { data ->
            val msg = ChatMessageModel(data.senderId == authUserId, data.content, data.sentAt)
            _realTimeMessages.value = listOf(msg) + _realTimeMessages.value
        }
    }

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