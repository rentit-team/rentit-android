package com.example.rentit.presentation.chat.chatroom

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.chat.dto.MessageResponseDto
import com.example.rentit.domain.chat.exception.ForbiddenChatAccessException
import com.example.rentit.domain.chat.usecase.ConvertMessageUseCase
import com.example.rentit.domain.chat.usecase.GetChatRoomDetailUseCase
import com.example.rentit.domain.chat.websocket.WebSocketManager
import com.example.rentit.domain.product.usecase.GetChatRoomProductSummaryUseCase
import com.example.rentit.domain.rental.usecase.GetChatRoomRentalSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

private const val TAG = "ChatRoomViewModel"

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val webSocketManager: WebSocketManager,
    private val convertMessageUseCase: ConvertMessageUseCase,
    private val getChatRoomRentalSummaryUseCase: GetChatRoomRentalSummaryUseCase,
    private val getChatRoomProductSummaryUseCase: GetChatRoomProductSummaryUseCase,
    private val getChatRoomDetailUseCase: GetChatRoomDetailUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(ChatRoomState())
    val uiState: StateFlow<ChatRoomState> = _uiState

    private val _sideEffect = MutableSharedFlow<ChatRoomSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private fun resetMessages() {
        _uiState.value = _uiState.value.copy(messages = emptyList())
    }

    private fun showServerErrorDialog() {
        _uiState.value = _uiState.value.copy(showServerErrorDialog = true)
    }

    private fun showNetworkErrorDialog() {
        _uiState.value = _uiState.value.copy(showNetworkErrorDialog = true)
    }

    private fun showForbiddenChatAccessDialog() {
        _uiState.value = _uiState.value.copy(showForbiddenChatAccessDialog = true)
    }

    private fun setIsLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    private fun emitSideEffect(sideEffect: ChatRoomSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    private fun onMessageReceived(message: MessageResponseDto) {
        val previousMessages = _uiState.value.messages
        val newMessage = convertMessageUseCase.execute(message)

        _uiState.value = _uiState.value.copy(messages = listOf(newMessage) + previousMessages)
        emitSideEffect(ChatRoomSideEffect.MessageReceived)
    }

    suspend fun fetchChatRoomData(chatRoomId: String) {
        setIsLoading(true)
        runCatching {
            val chatRoomDetail = getChatRoomDetailUseCase(chatRoomId).getOrThrow()
            val productSummary = getChatRoomProductSummaryUseCase(chatRoomDetail.productId).getOrThrow()
            val rentalSummary = getChatRoomRentalSummaryUseCase(chatRoomDetail.productId,chatRoomDetail.reservationId).getOrThrow()
            _uiState.value = _uiState.value.copy(
                partnerNickname = chatRoomDetail.partnerNickname,
                messages = chatRoomDetail.messages,
                productSummary = productSummary,
                rentalSummary = rentalSummary
            )
        }.onFailure { e ->
            when (e) {
                is IOException -> {
                    Log.e(TAG, "네트워크 오류 발생", e)
                    showNetworkErrorDialog()
                }
                is ForbiddenChatAccessException -> {
                    Log.e(TAG, "채팅방 접근 권한 없음", e)
                    showForbiddenChatAccessDialog()
                }
                else -> {
                    Log.e(TAG, "서버 오류 발생", e)
                    showServerErrorDialog()
                }
                // TODO: 토큰 에러 처리 필요 (리프레시 토큰으로 재발급 또는 로그인 화면 이동)
            }
        }
        setIsLoading(false)
    }

    fun retryFetchChatRoomData(chatRoomId: String){
        _uiState.value = _uiState.value.copy(showServerErrorDialog = false, showNetworkErrorDialog = false)
        viewModelScope.launch {
            fetchChatRoomData(chatRoomId)
        }
    }

    fun connectWebSocket(chatRoomId: String) {
        webSocketManager.connect(
            chatroomId = chatRoomId,
            onMessageReceived = ::onMessageReceived,
            onError = { emitSideEffect(ChatRoomSideEffect.ToastChatDisconnect) }
        )
    }

    fun sendMessage(chatRoomId: String, message: String) {
        if(message.isNotBlank()){
            webSocketManager.sendMessage(chatRoomId, message,
                onSuccess = { emitSideEffect(ChatRoomSideEffect.MessageSendSuccess) },
                onError = { emitSideEffect(ChatRoomSideEffect.ToastMessageSendFailed) }
            )
        }
    }

    fun disconnectWebSocket() {
        webSocketManager.disconnect()
        resetMessages()
    }
}