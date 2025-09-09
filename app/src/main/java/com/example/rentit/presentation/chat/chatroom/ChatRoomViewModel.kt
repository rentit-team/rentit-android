package com.example.rentit.presentation.chat.chatroom

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.core.error.ForbiddenException
import com.example.rentit.data.chat.dto.MessageResponseDto
import com.example.rentit.domain.chat.usecase.ConvertMessageUseCase
import com.example.rentit.domain.chat.usecase.GetChatRoomDetailUseCase
import com.example.rentit.domain.chat.websocket.WebSocketManager
import com.example.rentit.domain.product.model.PaymentValidationResult
import com.example.rentit.domain.product.usecase.GetChatRoomProductSummaryUseCase
import com.example.rentit.domain.product.usecase.ValidatePaymentProcessUseCase
import com.example.rentit.domain.rental.usecase.GetChatRoomRentalSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "ChatRoomViewModel"

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val webSocketManager: WebSocketManager,
    private val convertMessageUseCase: ConvertMessageUseCase,
    private val validatePaymentProcessUseCase: ValidatePaymentProcessUseCase,
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
                Log.e(TAG, "채팅방 로드 에러", e)
            when (e) {
                is ForbiddenException -> {
                    showForbiddenChatAccessDialog()
                }
                else -> {
                    emitSideEffect(ChatRoomSideEffect.CommonError(e))
                }
            }
        }
        setIsLoading(false)
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

    fun retryFetchChatRoomData(chatRoomId: String){
        viewModelScope.launch {
            fetchChatRoomData(chatRoomId)
            connectWebSocket(chatRoomId)
        }
    }
}