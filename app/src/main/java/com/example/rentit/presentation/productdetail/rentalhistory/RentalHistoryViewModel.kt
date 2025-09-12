package com.example.rentit.presentation.productdetail.rentalhistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private fun initCalendarMonth() {
        val list = _uiState.value.rentalHistoryList.firstOrNull()
        val initMonth = list?.rentalPeriod?.startDate
        initMonth?.let { updateUiState { copy(calendarMonth = YearMonth.from(it)) } }
    }

    suspend fun getProductRequestList(productId: Int){
        getRentalHistoriesUseCase(productId)
            .onSuccess {
                updateUiState { copy(rentalHistoryList = it) }
                initCalendarMonth()
            }.onFailure {

            }
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
    }

    fun onHistoryClicked(reservationId: Int) {
        emitSideEffect(RentalHistorySideEffect.NavigateToRentalDetail(reservationId))
    }

    suspend fun scrollToTop() {
        _uiState.value.historyListScrollState.animateScrollToItem(0)
    }
}