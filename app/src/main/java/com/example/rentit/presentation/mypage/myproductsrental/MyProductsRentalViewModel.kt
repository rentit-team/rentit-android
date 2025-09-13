package com.example.rentit.presentation.mypage.myproductsrental

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.domain.user.model.MyProductsRentalModel
import com.example.rentit.domain.user.usecase.GetMyProductsRentalHistoryUseCase
import com.example.rentit.presentation.mypage.myproductsrental.model.MyProductsRentalFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MyProductsRentalViewModel @Inject constructor(
    private val getMyProductsRentalHistoryUseCase: GetMyProductsRentalHistoryUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(MyProductsRentalState())
    val uiState: StateFlow<MyProductsRentalState> = _uiState

    private fun updateUiState(transform: MyProductsRentalState.() -> MyProductsRentalState) {
        _uiState.value = _uiState.value.transform()
    }

    fun getMyProductsRentalHistories() {
        viewModelScope.launch {
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
                    println(it)
                }
        }
    }

    fun onFilterChanged(filter: MyProductsRentalFilter) {
        updateUiState { copy(selectedFilter = filter) }
    }
}