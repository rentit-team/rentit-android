package com.example.rentit.presentation.productdetail.reservation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.core.exceptions.ConflictException
import com.example.rentit.core.exceptions.ForbiddenException
import com.example.rentit.domain.product.repository.ProductRepository
import com.example.rentit.domain.product.usecase.PostReservationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "ReservationViewModel"
private const val DEFAULT_DEPOSIT_DAYS = 3 // 기본 보증금 계산 기준 일수

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ReservationViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val postReservationUseCase: PostReservationUseCase
): ViewModel() {

    private val _uiState =  MutableStateFlow(ReservationState())
    val uiState: StateFlow<ReservationState> = _uiState

    private val _sideEffect = MutableSharedFlow<ReservationSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private fun emitSideEffect(sideEffect: ReservationSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    private suspend fun getProductDetail(productId: Int) {
        productRepository.getProductDetail(productId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    rentalPrice = it.product.price,
                    deposit = it.product.price * DEFAULT_DEPOSIT_DAYS,
                    minPeriod = it.product.period?.min,
                    maxPeriod = it.product.period?.max
                )
                Log.i(TAG, "상품 조회 성공: Product Id: $productId")
            }.onFailure { e ->
                Log.e(TAG, "상품 조회 실패", e)
                emitSideEffect(ReservationSideEffect.CommonError(e))
            }
    }

    private suspend fun getReservedDates(productId: Int) {
        productRepository.getReservedDates(productId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(reservedDateList = it.disabledDates)
                Log.i(TAG, "예약 불가일 조회 성공: 불가일 ${it.disabledDates.size}개")
            }.onFailure { e ->
                Log.e(TAG, "예약 불가일 조회 실패", e)
                emitSideEffect(ReservationSideEffect.CommonError(e))
            }
    }

    suspend fun loadInitialData(productId: Int) {
        setLoading(true)
        getProductDetail(productId)
        getReservedDates(productId)
        setLoading(false)
    }

    fun retryDataLoad(productId: Int) {
        viewModelScope.launch {
            loadInitialData(productId)
        }
    }

    fun postResv(productId: Int){
        val startDate = _uiState.value.rentalStartDate.toString()
        val endDate = _uiState.value.rentalEndDate.toString()
        val selectedPeriod = _uiState.value.selectedPeriod
        val minPeriod = _uiState.value.minPeriod
        val maxPeriod = _uiState.value.maxPeriod

        viewModelScope.launch {
            setLoading(true)
            setRequestButtonEnable(false)
            postReservationUseCase(
                productId = productId,
                minPeriod = minPeriod,
                maxPeriod = maxPeriod,
                selectedPeriod = selectedPeriod,
                startDate = startDate,
                endDate = endDate
            ).onSuccess {
                emitSideEffect(
                    ReservationSideEffect.NavigateToReservationComplete(
                        reservationId = it.data.reservationId,
                        rentalStartDate = startDate,
                        rentalEndDate = endDate,
                        totalPrice = it.data.totalAmount
                    )
                )
                Log.i(TAG, "대여 예약 성공: ${it.data.reservationId}")
            }.onFailure { e ->
                Log.e(TAG, "대여 예약 실패", e)
                handlePostResvError(e)
            }
            setRequestButtonEnable(true)
            setLoading(false)
        }
    }

    private fun handlePostResvError(e: Throwable) {
        when(e) {
            is ForbiddenException -> {
                _uiState.value = _uiState.value.copy(showAccessNotAllowedDialog = true)
            }
            is ConflictException -> {
                _uiState.value = _uiState.value.copy(showResvAlreadyExistDialog = true)
            }
            is IllegalArgumentException -> {
                emitSideEffect(ReservationSideEffect.ToastInvalidPeriod)
            }
            else -> {
                emitSideEffect(ReservationSideEffect.ToastPostReservationFailed)
            }
        }
    }

    fun setRentalStartDate(date: LocalDate?) {
        _uiState.value = _uiState.value.copy(rentalStartDate = date)
    }

    fun setRentalEndDate(date: LocalDate?) {
        _uiState.value = _uiState.value.copy(rentalEndDate = date)
    }

    fun dismissAccessNotAllowedDialog() {
        _uiState.value = _uiState.value.copy(showAccessNotAllowedDialog = false)
    }

    fun dismissResvAlreadyExistDialog() {
        _uiState.value = _uiState.value.copy(showResvAlreadyExistDialog = false)
    }

    fun setRequestButtonEnable(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isRequestButtonEnabled = enabled)
    }
}