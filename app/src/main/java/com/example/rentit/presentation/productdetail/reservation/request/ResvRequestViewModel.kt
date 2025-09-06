package com.example.rentit.presentation.productdetail.reservation.request

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.dto.ResvRequestDto
import com.example.rentit.domain.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "ResvRequestViewModel"
private const val DEFAULT_DEPOSIT_DAYS = 3 // 기본 보증금 계산 기준 일수

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ResvRequestViewModel @Inject constructor(
    private val productRepository: ProductRepository,
): ViewModel() {

    private val _uiState =  MutableStateFlow(ResvRequestState())
    val uiState: StateFlow<ResvRequestState> = _uiState

    private val _sideEffect = MutableSharedFlow<ResvRequestSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private fun emitSideEffect(sideEffect: ResvRequestSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    fun setRentalStartDate(date: LocalDate?) {
        _uiState.value = _uiState.value.copy(rentalStartDate = date)
    }

    fun setRentalEndDate(date: LocalDate?) {
        _uiState.value = _uiState.value.copy(rentalEndDate = date)
    }

    fun getProductDetail(productId: Int) {
        viewModelScope.launch {
            productRepository.getProductDetail(productId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        rentalPrice = it.product.price,
                        deposit = it.product.price * DEFAULT_DEPOSIT_DAYS,
                        minPeriod = it.product.period?.min,
                        maxPeriod = it.product.period?.max
                    )
                }.onFailure { }
        }
    }

    fun getReservedDates(productId: Int) {
        viewModelScope.launch {
            productRepository.getReservedDates(productId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(reservedDateList = it.disabledDates)
                }.onFailure { }
        }
    }

    fun postResv(productId: Int){
        val startDate = _uiState.value.rentalStartDate.toString()
        val endDate = _uiState.value.rentalEndDate.toString()
        val totalPrice = _uiState.value.totalPrice

        val request = ResvRequestDto(startDate, endDate)

        viewModelScope.launch {
            productRepository.postResv(productId, request)
                .onSuccess {
                    emitSideEffect(ResvRequestSideEffect.NavigateToResvRequestComplete(
                        rentalStartDate = startDate,
                        rentalEndDate = endDate,
                        totalPrice = totalPrice
                    ))
                }.onFailure { }
        }
    }
}