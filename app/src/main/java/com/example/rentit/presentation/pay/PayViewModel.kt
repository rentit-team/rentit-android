package com.example.rentit.presentation.pay

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.RentalStatus
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

@HiltViewModel
class PayViewModel @Inject constructor(
    private val rentalRepository: RentalRepository
): ViewModel() {

    private val _isDialogVisible = MutableStateFlow(false)
    val isDialogVisible: StateFlow<Boolean> = _isDialogVisible

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _sideEffect = MutableSharedFlow<PaySideEffect>()
    val sideEffect: SharedFlow<PaySideEffect> = _sideEffect

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateStatusToPaid(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            setLoading(true)
            delay(500)     // 즉시 전환 방지를 위한 최소 로딩
            rentalRepository.updateRentalStatus(
                productId,
                reservationId,
                UpdateRentalStatusRequestDto(RentalStatus.PAID)
            ).onSuccess {
                setDialogVisibility(true)
            }.onFailure {
                setDialogVisibility(true)
                toastPayFailed()
            }
            setLoading(false)
        }
    }

    private suspend fun toastPayFailed() {
        _sideEffect.emit(PaySideEffect.ToastPayFailed)
    }

    private fun setLoading(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    fun setDialogVisibility(isVisible: Boolean) {
        _isDialogVisible.value = isVisible
    }

    fun onConfirmAndNavigateBack() {
        navigateBackToRentalDetail()
        setDialogVisibility(false)
    }

    fun navigateBackToRentalDetail() {
        viewModelScope.launch {
            _sideEffect.emit(PaySideEffect.NavigateBackToRentalDetail)
        }
    }
}