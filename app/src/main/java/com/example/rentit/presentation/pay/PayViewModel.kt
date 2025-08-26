package com.example.rentit.presentation.pay

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.model.RentalSummaryUiModel
import com.example.rentit.data.rental.dto.UpdateRentalStatusRequestDto
import com.example.rentit.data.rental.repository.RentalRepository
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
    private val rentalRepository: RentalRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(PayState())
    val uiState: StateFlow<PayState> = _uiState

    private val _sideEffect = MutableSharedFlow<PaySideEffect>()
    val sideEffect: SharedFlow<PaySideEffect> = _sideEffect

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
                _uiState.value = _uiState.value.copy(
                    rentalSummary = rentalSummary,
                    depositAmount = rental.depositAmount
                )
            }.onFailure {
                showLoadErrorDialog()
            }
        setLoading(false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStatusToPaid(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            setLoading(true)
            delay(MIN_LOADING_DURATION)
            try {
                rentalRepository.updateRentalStatus(
                    productId,
                    reservationId,
                    UpdateRentalStatusRequestDto(RentalStatus.PAID)
                ).onSuccess {
                    showPayResultDialog()
                }.onFailure {
                    toastPayFailed()
                }
            } finally {
                setLoading(false)
            }
        }
    }

    private suspend fun toastPayFailed() {
        _sideEffect.emit(PaySideEffect.ToastPayFailed)
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    private fun showLoadErrorDialog() {
        _uiState.value = _uiState.value.copy(showLoadErrorDialog = true)
    }

    fun dismissLoadErrorDialogAndNavigateBack() {
        _uiState.value = _uiState.value.copy(showLoadErrorDialog = false)
        navigateBackToRentalDetail()
    }

    fun showPayResultDialog() {
        _uiState.value = _uiState.value.copy(showPayResultDialog = true)
    }

    private fun hidePayResultDialog() {
        _uiState.value = _uiState.value.copy(showPayResultDialog = false)
    }

    fun onConfirmAndNavigateBack() {
        hidePayResultDialog()
        navigateBackToRentalDetail()
    }

    fun navigateBackToRentalDetail() {
        viewModelScope.launch {
            _sideEffect.emit(PaySideEffect.NavigateBackToRentalDetail)
        }
    }
}