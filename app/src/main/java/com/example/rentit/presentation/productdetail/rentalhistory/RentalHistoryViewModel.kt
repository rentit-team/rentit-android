package com.example.rentit.presentation.productdetail.rentalhistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.rentit.domain.rental.usecase.GetRentalHistoriesUseCase
import com.example.rentit.presentation.productdetail.rentalhistory.model.RentalHistoryFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RentalHistoryViewModel @Inject constructor(
    private val getRentalHistoriesUseCase: GetRentalHistoriesUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow(RentalHistoryState())
    val uiState: StateFlow<RentalHistoryState> = _uiState

    private fun updateUiState(transform: RentalHistoryState.() -> RentalHistoryState) {
        _uiState.value = _uiState.value.transform()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getProductRequestList(productId: Int){
        getRentalHistoriesUseCase(productId)
            .onSuccess {
                updateUiState { copy(rentalHistoryList = it) }
            }.onFailure {

            }
        }*/
    }

    fun updateFilterMode(filterMode: RentalHistoryFilter){
        updateUiState {
            copy(
                filterMode = filterMode,
                selectedReservationId = null
            )
        }
    }
}