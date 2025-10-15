package com.example.rentit.presentation.mypage.myproductsrental

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.domain.user.model.MyProductsRentalModel
import com.example.rentit.domain.user.usecase.GetMyProductsRentalHistoryUseCase
import com.example.rentit.presentation.mypage.myproductsrental.model.MyProductsRentalFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MyProductsRentalViewModel"

@HiltViewModel
class MyProductsRentalViewModel @Inject constructor(
    private val getMyProductsRentalHistoryUseCase: GetMyProductsRentalHistoryUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(MyProductsRentalState())
    val uiState: StateFlow<MyProductsRentalState> = _uiState

    private val _sideEffect = MutableSharedFlow<MyProductsRentalSideEffect>()
    val sideEffect: SharedFlow<MyProductsRentalSideEffect> = _sideEffect

    private fun updateUiState(transform: MyProductsRentalState.() -> MyProductsRentalState) {
        _uiState.value = _uiState.value.transform()
    }

    private fun emitSideEffect(sideEffect: MyProductsRentalSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        updateUiState { copy(isLoading = isLoading) }
    }

    suspend fun getMyProductsRentalHistories() {
        setLoading(true)
        getMyProductsRentalHistoryUseCase()
            .onSuccess {
                val historiesByFilter: Map<MyProductsRentalFilter, List<MyProductsRentalModel>> = it.mapKeys { (status, _) ->
                    when (status) {
                        RentalStatus.PENDING -> MyProductsRentalFilter.WAITING_FOR_RESPONSE
                        RentalStatus.PAID -> MyProductsRentalFilter.WAITING_FOR_SHIPMENT
                        RentalStatus.RENTING -> MyProductsRentalFilter.RENTING
                        RentalStatus.ACCEPTED -> MyProductsRentalFilter.ACCEPTED
                        else -> MyProductsRentalFilter.NONE
                    }
                }
                updateUiState { copy(rentalHistories = historiesByFilter) }
                Log.i(TAG, "내 상품에 대한 대여 내역 조회 성공: 총 ${it.size}개")
            }.onFailure { e ->
                Log.e(TAG, "내 상품에 대한 대여 내역 조회 실패", e)
                emitSideEffect(MyProductsRentalSideEffect.CommonError(e))
            }
        setLoading(false)
    }

    fun reloadData() {
        viewModelScope.launch {
            getMyProductsRentalHistories()
        }
    }

    fun onFilterChanged(filter: MyProductsRentalFilter) {
        updateUiState { copy(selectedFilter = filter, showNoticeBanner = false) }
        when (filter) {
            MyProductsRentalFilter.WAITING_FOR_RESPONSE, MyProductsRentalFilter.WAITING_FOR_SHIPMENT -> {
                updateUiState { copy(showNoticeBanner = true) }
            }
            else -> {}
        }
    }

    fun onItemClicked(selectedFilter: MyProductsRentalFilter, productId: Int, reservationId: Int) {
        when (selectedFilter) {
            MyProductsRentalFilter.WAITING_FOR_SHIPMENT, MyProductsRentalFilter.RENTING -> {
                emitSideEffect(MyProductsRentalSideEffect.NavigateToProductDetail(productId, reservationId, null))
            }
            MyProductsRentalFilter.WAITING_FOR_RESPONSE, MyProductsRentalFilter.ACCEPTED -> {
                val rentalStatus = when (selectedFilter) {
                    MyProductsRentalFilter.WAITING_FOR_RESPONSE -> RentalStatus.PENDING
                    MyProductsRentalFilter.ACCEPTED -> RentalStatus.ACCEPTED
                    else -> null
                }
                emitSideEffect(MyProductsRentalSideEffect.NavigateToProductDetail(productId, null, rentalStatus))
            }
            else -> {}
        }
    }
}