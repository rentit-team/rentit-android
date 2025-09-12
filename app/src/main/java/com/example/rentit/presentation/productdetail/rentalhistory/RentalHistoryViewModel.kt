package com.example.rentit.presentation.productdetail.rentalhistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.uimodel.RentalPeriodModel
import com.example.rentit.domain.product.repository.ProductRepository
import com.example.rentit.domain.rental.model.RentalHistoryModel
import com.example.rentit.presentation.productdetail.rentalhistory.model.RentalHistoryFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class RentalHistoryViewModel @Inject constructor(
    private val productRepository: ProductRepository,
): ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    private val sampleRentalHistoryList = listOf(
        RentalHistoryModel(105, 505, RentalStatus.RENTING, "Eve", RentalPeriodModel(
            LocalDate.parse("2025-09-14"), LocalDate.parse("2025-09-16")), "2025-09-05T14:00:00"),
        RentalHistoryModel(109, 509, RentalStatus.PAID, "Ian", RentalPeriodModel(LocalDate.parse("2025-09-18"), LocalDate.parse("2025-09-20")), "2025-09-09T18:10:00"),
        RentalHistoryModel(104, 504, RentalStatus.PAID, "David", RentalPeriodModel(LocalDate.parse("2025-09-13"), LocalDate.parse("2025-09-14")), "2025-09-04T13:45:00"),
        RentalHistoryModel(101, 501, RentalStatus.PENDING, "Alice", RentalPeriodModel(
            LocalDate.parse("2025-09-10"), LocalDate.parse("2025-09-12")), "2025-09-01T10:00:00"),
        RentalHistoryModel(102, 502, RentalStatus.ACCEPTED, "Bob", RentalPeriodModel(
            LocalDate.parse("2025-09-11"), LocalDate.parse("2025-09-13")), "2025-09-02T11:15:00"),
        RentalHistoryModel(103, 503, RentalStatus.REJECTED, "Charlie", RentalPeriodModel(
            LocalDate.parse("2025-10-12"), LocalDate.parse("2025-10-15")), "2025-09-03T12:30:00"),
        RentalHistoryModel(106, 506, RentalStatus.RETURNED, "Frank", RentalPeriodModel(
            LocalDate.parse("2025-10-15"), LocalDate.parse("2025-10-17")), "2025-09-06T15:20:00"),
        RentalHistoryModel(107, 507, RentalStatus.CANCELED, "Grace", RentalPeriodModel(
            LocalDate.parse("2025-09-16"), LocalDate.parse("2025-09-18")), "2025-09-07T16:40:00"),
        RentalHistoryModel(110, 510, RentalStatus.RETURNED, "Jane", RentalPeriodModel(
            LocalDate.parse("2025-09-07"), LocalDate.parse("2025-09-09")), "2025-09-10T19:30:00")
    )

    private val _uiState = MutableStateFlow(RentalHistoryState())
    val uiState: StateFlow<RentalHistoryState> = _uiState

    private fun updateUiState(transform: RentalHistoryState.() -> RentalHistoryState) {
        _uiState.value = _uiState.value.transform()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getProductRequestList(productId: Int){
        updateUiState {
            copy(
                rentalHistoryList = sampleRentalHistoryList
            )
        }
        /*viewModelScope.launch {
            productRepository.getProductRequestList(productId).onSuccess {

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