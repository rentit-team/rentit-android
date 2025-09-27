package com.example.rentit.presentation.chat.chatroom

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.core.exceptions.ForbiddenException
import com.example.rentit.data.chat.dto.MessageResponseDto
import com.example.rentit.domain.chat.repository.ChatRepository
import com.example.rentit.domain.chat.usecase.ConvertMessageUseCase
import com.example.rentit.domain.chat.usecase.GetChatRoomDetailUseCase
import com.example.rentit.domain.product.model.PaymentValidationResult
import com.example.rentit.domain.product.usecase.ValidatePaymentProcessUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ChatRoomViewModel"

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val convertMessageUseCase: ConvertMessageUseCase,
    private val validatePaymentProcessUseCase: ValidatePaymentProcessUseCase,
    private val getChatRoomDetailUseCase: GetChatRoomDetailUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(ChatRoomState())
    val uiState: StateFlow<ChatRoomState> = _uiState

    private val _sideEffect = MutableSharedFlow<ChatRoomSideEffect>()
    val sideEffect: SharedFlow<ChatRoomSideEffect> = _sideEffect

    private fun updateUiState(transform: ChatRoomState.() -> ChatRoomState) {
        _uiState.update(transform)
    }

    private fun emitSideEffect(sideEffect: ChatRoomSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    suspend fun fetchChatRoomData(chatRoomId: String) {
        updateUiState { copy(isLoading = true) }
        getChatRoomDetailUseCase(chatRoomId)
            .onSuccess {
                updateUiState { copy(
                    partnerNickname = it.partnerNickname,
                    messages = it.messages,
                    productSummary = it.productSummary,
                    rentalSummary = it.rentalSummary
                ) }
                Log.i(TAG, "채팅방 데이터 로드 성공")
            }.onFailure { e ->
                Log.e(TAG, "채팅방 데이터 로드 실패", e)
                when (e) {
                    is ForbiddenException -> updateUiState { copy(showForbiddenChatAccessDialog = true) }
                    else -> emitSideEffect(ChatRoomSideEffect.CommonError(e))
                }
            }
        updateUiState { copy(isLoading = false) }
    }

    fun onPayClicked() {
        val productId = _uiState.value.productSummary?.productId
        val reservationId = _uiState.value.rentalSummary?.reservationId
        val rentalStatus = _uiState.value.rentalSummary?.status
        val renterId = _uiState.value.rentalSummary?.renterId

        val validationResult = validatePaymentProcessUseCase(productId, reservationId, renterId, rentalStatus)

        when (validationResult) {
            PaymentValidationResult.Success -> {
                emitSideEffect(ChatRoomSideEffect.NavigateToPay(productId!!, reservationId!!))
            }
            PaymentValidationResult.InvalidStatus -> {
                emitSideEffect(ChatRoomSideEffect.ToastPaymentInvalidStatus)
            }
            PaymentValidationResult.NotRenter -> {
                emitSideEffect(ChatRoomSideEffect.ToastPaymentNotRenter)
            }
            PaymentValidationResult.ProductNotFound -> {
                emitSideEffect(ChatRoomSideEffect.ToastPaymentProductNotFound)
            }
        }
    }

    fun onProductSectionClicked() {
        val productId = _uiState.value.productSummary?.productId
        if (productId != null) {
            emitSideEffect(ChatRoomSideEffect.NavigateToProductDetail(productId))
        }
    }

    fun onRentalSectionClicked() {
        val reservationId = _uiState.value.rentalSummary?.reservationId
        val productId = _uiState.value.productSummary?.productId
        if (reservationId != null && productId != null) {
            emitSideEffect(ChatRoomSideEffect.NavigateToRentalDetail(productId, reservationId))
        }
    }

    fun retryFetchChatRoomData(chatRoomId: String){
        viewModelScope.launch {
            fetchChatRoomData(chatRoomId)
            connectWebSocket(chatRoomId)
        }
    }

    /** WebSocket 채팅 */
    private fun onMessageReceived(message: MessageResponseDto) {
        val previousMessages = _uiState.value.messages
        val newMessage = convertMessageUseCase(message)

        updateUiState { copy(messages = listOf(newMessage) + previousMessages) }
        emitSideEffect(ChatRoomSideEffect.MessageReceived)
    }

    fun connectWebSocket(chatRoomId: String) {
        chatRepository.connectWebSocket(
            chatRoomId = chatRoomId,
            onMessageReceived = ::onMessageReceived,
            onError = { emitSideEffect(ChatRoomSideEffect.ToastChatDisconnect) }
        )
    }

    fun sendMessage(chatRoomId: String, message: String) {
        if(message.isBlank()) return

        chatRepository.sendMessage(
            chatRoomId = chatRoomId,
            message = message,
            onSuccess = { emitSideEffect(ChatRoomSideEffect.MessageSendSuccess) },
            onError = { emitSideEffect(ChatRoomSideEffect.ToastMessageSendFailed) }
        )
    }

    fun disconnectWebSocket() {
        chatRepository.disconnectWebSocket()
        updateUiState { copy(messages = emptyList()) }
    }
}