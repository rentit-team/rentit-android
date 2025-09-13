package com.example.rentit.presentation.productdetail.rentalhistory

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.core.error.ForbiddenException
import com.example.rentit.domain.rental.usecase.GetRentalHistoriesUseCase
import com.example.rentit.presentation.productdetail.rentalhistory.model.RentalHistoryFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

private const val TAG = "RentalHistoryViewModel"

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class RentalHistoryViewModel @Inject constructor(
    private val getRentalHistoriesUseCase: GetRentalHistoriesUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(RentalHistoryState())
    val uiState: StateFlow<RentalHistoryState> = _uiState

    private val _sideEffect = MutableSharedFlow<RentalHistorySideEffect>()
    val sideEffect: SharedFlow<RentalHistorySideEffect> = _sideEffect

    private fun updateUiState(transform: RentalHistoryState.() -> RentalHistoryState) {
        _uiState.value = _uiState.value.transform()
    }

    private fun emitSideEffect(effect: RentalHistorySideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(effect)
        }
    }

    private fun setIsLoading(isLoading: Boolean) {
        updateUiState { copy(isLoading = isLoading) }
    }

    fun initializeFiltering(initialFilterMode: RentalStatus?, reservationId: Int?) {
        reservationId?.let { updateSelectedReservationId(reservationId) }

        initialFilterMode?.let {
            val finishedStatuses = listOf(RentalStatus.CANCELED, RentalStatus.REJECTED, RentalStatus.RETURNED)
            when(it) {
                !in finishedStatuses -> updateFilterMode(RentalHistoryFilter.IN_PROGRESS)
                RentalStatus.PENDING -> updateFilterMode(RentalHistoryFilter.REQUEST)
                RentalStatus.ACCEPTED -> updateFilterMode(RentalHistoryFilter.ACCEPTED)
                else -> updateFilterMode(RentalHistoryFilter.FINISHED)
            }
        }
    }

    suspend fun loadProductRentalHistories(productId: Int){
        setIsLoading(true)
        getRentalHistoriesUseCase(productId)
            .onSuccess {
                updateUiState { copy(rentalHistoryList = it) }
                Log.i(TAG, "상품 대여 내역 조회 성공: 총 ${it.size}개")
            }.onFailure { e ->
                Log.e(TAG, "상품 대여 내역 조회 실패", e)
                if(e is ForbiddenException) {
                    updateUiState { copy(showAccessForbiddenDialog = true) }
                } else {
                    emitSideEffect(RentalHistorySideEffect.CommonError(e))
                }
            }
        setIsLoading(false)
    }

    fun updateCalendarMonth(month: Long) {
        updateUiState { copy(calendarMonth = calendarMonth.plusMonths(month)) }
    }

    fun updateSelectedRentalDate(reservationId: Int) {
        val currentId = uiState.value.selectedReservationId
        currentId?.let {
            updateUiState { copy(selectedReservationId = null) }
        } ?: updateUiState { copy(selectedReservationId = reservationId) }
    }

    fun updateFilterMode(filterMode: RentalHistoryFilter){
        updateUiState {
            copy(
                filterMode = filterMode,
                selectedReservationId = null
            )
        }
        emitSideEffect(RentalHistorySideEffect.ScrollToTop)
    }

    fun updateSelectedReservationId(reservationId: Int) {
        val currentId = uiState.value.selectedReservationId
        currentId?.let {
            updateUiState { copy(selectedReservationId = null, calendarMonth = YearMonth.now()) }
        } ?: run {
            updateUiState { copy(selectedReservationId = reservationId) }
            val startDate = uiState.value.filteredRentalHistoryList.firstOrNull()?.rentalPeriod?.startDate
            startDate?.let { updateUiState { copy(calendarMonth = YearMonth.from(it)) } }
        }
    }

    fun onRentalDetailClicked(reservationId: Int) {
        emitSideEffect(RentalHistorySideEffect.NavigateToRentalDetail(reservationId))
    }

    fun retryLoadHistories(productId: Int) {
        viewModelScope.launch {
            loadProductRentalHistories(productId)
        }
    }
}