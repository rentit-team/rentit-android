package com.example.rentit.presentation.pay

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.AutoMessageType
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.presentation.rentaldetail.model.RentalSummaryUiModel
import com.example.rentit.data.rental.dto.UpdateRentalStatusRequestDto
import com.example.rentit.domain.chat.repository.ChatRepository
import com.example.rentit.domain.rental.repository.RentalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MIN_LOADING_DURATION = 500L    // 즉시 전환 방지를 위한 최소 로딩

@HiltViewModel
class PayViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val rentalRepository: RentalRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(PayState())
    val uiState: StateFlow<PayState> = _uiState

    private val _sideEffect = MutableSharedFlow<PaySideEffect>()
    val sideEffect: SharedFlow<PaySideEffect> = _sideEffect

    private var chatRoomId: String? = null

    private fun emitSideEffect(sideEffect: PaySideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getPayInfo(productId: Int, reservationId: Int) {
        setLoading(true)
        rentalRepository.getRentalDetail(productId, reservationId)
            .onSuccess {
                val rental = it.rental
                val rentalSummary = RentalSummaryUiModel(
                    productTitle = rental.product.title,
                    thumbnailImgUrl = rental.product.thumbnailImgUrl,
                    startDate = rental.startDate,
                    endDate = rental.endDate,
                    totalPrice = rental.totalAmount
                )
                chatRoomId = it.rental.chatroomId
                _uiState.value = _uiState.value.copy(
                    rentalSummary = rentalSummary,
                    depositAmount = rental.depositAmount
                )
            }.onFailure {
                emitSideEffect(PaySideEffect.CommonError(it))
            }
        setLoading(false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStatusToPaid(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            setLoading(true)
            delay(MIN_LOADING_DURATION)
            rentalRepository.updateRentalStatus(
                productId,
                reservationId,
                UpdateRentalStatusRequestDto(RentalStatus.PAID)
            ).onSuccess {
                showPayResultDialog()
                chatRoomId?.let {
                    connectWebSocket(it)
                    sendPayCompleteMessage(it)
                }
            }.onFailure {
                emitSideEffect(PaySideEffect.ToastPayFailed)
            }
            setLoading(false)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    fun showPayResultDialog() {
        _uiState.value = _uiState.value.copy(showPayResultDialog = true)
    }

    private fun hidePayResultDialog() {
        _uiState.value = _uiState.value.copy(showPayResultDialog = false)
    }

    fun onConfirmAndNavigateBack() {
        hidePayResultDialog()
        emitSideEffect(PaySideEffect.NavigateBack)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun reloadData(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            getPayInfo(productId, reservationId)
        }
    }

    /**
     * WebSocket 채팅: 결제 완료 자동 메시지 전송
     * - 옵션 기능이므로 에러 발생 시 별도로 대응하지 않음
     */
    private fun connectWebSocket(chatRoomId: String) {
        chatRepository.connectWebSocket(
            chatRoomId = chatRoomId,
            onMessageReceived = { },
            onError = { }
        )
    }

    private fun sendPayCompleteMessage(chatRoomId: String) {
        chatRepository.sendMessage(
            chatRoomId = chatRoomId,
            message = AutoMessageType.COMPLETE_PAY.code,
            onSuccess = {
                emitSideEffect(PaySideEffect.ToastPaidMessageSendSuccess)
                chatRepository.disconnectWebSocket()
            },
            onError = { }
        )
    }
}