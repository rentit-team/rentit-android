package com.example.rentit.presentation.productdetail.reservation.request

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.exception.MissingTokenException
import com.example.rentit.domain.product.exception.AccessNotAllowedException
import com.example.rentit.domain.product.exception.ResvAlreadyExistException
import com.example.rentit.domain.product.repository.ProductRepository
import com.example.rentit.domain.product.usecase.PostResvRequestUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okio.IOException
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "ResvRequestViewModel"
private const val DEFAULT_DEPOSIT_DAYS = 3 // 기본 보증금 계산 기준 일수

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ResvRequestViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val postResvRequestUseCase: PostResvRequestUseCase
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

    private fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    private fun handleFailure(e: Throwable) {
        when(e) {
            is MissingTokenException -> {
                // TODO: 로그아웃 처리 및 로그인 화면으로 이동
            }
            is AccessNotAllowedException -> {
                _uiState.value = _uiState.value.copy(showAccessNotAllowedDialog = true)
            }
            is ResvAlreadyExistException -> {
                _uiState.value = _uiState.value.copy(showResvAlreadyExistDialog = true)
            }
            is IllegalArgumentException -> {
                emitSideEffect(ResvRequestSideEffect.ToastInvalidPeriod)
            }
            is IOException -> {
                _uiState.value = _uiState.value.copy(showNetworkErrorDialog = true)
            }
            else -> {
                _uiState.value = _uiState.value.copy(showServerErrorDialog = true)
            }
        }
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
            }.onFailure { e -> handleFailure(e) }
    }

    private suspend fun getReservedDates(productId: Int) {
        productRepository.getReservedDates(productId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(reservedDateList = it.disabledDates)
            }.onFailure { e -> handleFailure(e) }
    }

    suspend fun loadInitialData(productId: Int) {
        setLoading(true)
        getProductDetail(productId)
        getReservedDates(productId)
        setLoading(false)
    }

    fun onRetryLoadData(productId: Int) {
        _uiState.value = _uiState.value.copy(
            showNetworkErrorDialog = false,
            showServerErrorDialog = false,
        )
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
        val totalPrice = _uiState.value.totalPrice

        viewModelScope.launch {
            postResvRequestUseCase(
                productId = productId,
                minPeriod = minPeriod,
                maxPeriod = maxPeriod,
                selectedPeriod = selectedPeriod,
                startDate = startDate,
                endDate = endDate
            ).onSuccess {
                emitSideEffect(
                    ResvRequestSideEffect.NavigateToResvRequestComplete(
                        rentalStartDate = startDate,
                        rentalEndDate = endDate,
                        totalPrice = totalPrice
                    )
                )
            }.onFailure { e -> handleFailure(e) }
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

    fun dismissAllDialogs() {
        _uiState.value = _uiState.value.copy(
            showNetworkErrorDialog = false,
            showServerErrorDialog = false,
            showAccessNotAllowedDialog = false,
            showResvAlreadyExistDialog = false
        )
    }
}