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

    private val _uiState = MutableStateFlow(ChatRoomState())
    val uiState: StateFlow<ChatRoomState> = _uiState

    suspend fun fetchChatDetail(chatRoomId: String) {
        setIsLoading(true)
        getChatRoomDetailUseCase(chatRoomId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    partnerNickname = it.partnerNickname,
                    messages = it.messages
                )
                fetchRentalSummary(it.productId, it.reservationId)
                fetchProductSummary(it.productId)
            }.onFailure { }
        setIsLoading(false)
    }

    private suspend fun fetchRentalSummary(productId: Int, reservationId: Int) {
        getChatRoomRentalSummaryUseCase(productId, reservationId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(rentalSummary = it)
            }.onFailure { }
    }

    private suspend fun fetchProductSummary(productId: Int) {
        getChatRoomProductSummaryUseCase(productId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(productSummary = it)
            }.onFailure { }
    }

    private fun setIsLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    fun connectWebSocket(chatRoomId: String, onConnect: () -> Unit) {
        val token = userRepository.getTokenFromPrefs() ?: return
        WebSocketManager.connect(chatRoomId, token, onConnect) { data ->
            val msg = ChatMessageModel(
                data.messageId,
                data.senderId == authUserId,
                data.content,
                data.sentAt
            )
            val previousMessages = _uiState.value.messages
            _uiState.value = _uiState.value.copy(messages = listOf(msg) + previousMessages)
        }
    }

    fun sendMessage(chatRoomId: String, message: String) {
        WebSocketManager.sendMessage(chatRoomId, authUserId, message)
    }

    fun disconnectWebSocket() {
        WebSocketManager.disconnect()
        resetMessages()
    }

    private fun resetMessages() {
        _uiState.value = _uiState.value.copy(messages = emptyList())
    }
}