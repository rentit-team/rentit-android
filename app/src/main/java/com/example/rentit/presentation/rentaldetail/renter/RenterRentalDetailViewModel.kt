package com.example.rentit.presentation.rentaldetail.renter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.rental.repository.RentalRepository
import com.example.rentit.presentation.rentaldetail.renter.stateui.RenterRentalStatusUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RenterRentalDetailViewModel @Inject constructor(
    private val rentalRepository: RentalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RenterRentalDetailState())
    val uiState: StateFlow<RenterRentalDetailState> = _uiState

    private val _sideEffect = MutableSharedFlow<RenterRentalDetailSideEffect>()
    val sideEffect: SharedFlow<RenterRentalDetailSideEffect> = _sideEffect

    @RequiresApi(Build.VERSION_CODES.O)
    private val _rentalDetailUiModel = MutableStateFlow<RenterRentalStatusUiModel>(RenterRentalStatusUiModel.Unknown)
    @RequiresApi(Build.VERSION_CODES.O)
    val rentalDetailUiModel: StateFlow<RenterRentalStatusUiModel> = _rentalDetailUiModel

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRentalDetail(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            rentalRepository.getRentalDetail(productId, reservationId)
                .onSuccess {
                    if(it.toRenterUiModel() != RenterRentalStatusUiModel.Unknown){
                        _rentalDetailUiModel.value = it.toRenterUiModel()
                    } else {
                        showUnknownStatusDialog()
                    }
                }.onFailure {
                    showUnknownStatusDialog()
                }
        }
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    private fun showUnknownStatusDialog() {
        _uiState.value = _uiState.value.copy(showUnknownStatusDialog = true)
    }

    fun showCancelDialog() {
        _uiState.value = _uiState.value.copy(showCancelDialog = true)
    }

    fun dismissCancelDialog() {
        _uiState.value = _uiState.value.copy(showCancelDialog = false)
    }

    fun confirmCancel() {
        /* 대여 취소 로직 추가, 성공 시 닫기 */
        _uiState.value = _uiState.value.copy(showCancelDialog = false)
    }

    fun showTrackingRegDialog() {
        _uiState.value = _uiState.value.copy(showTrackingRegDialog = true)
    }

    fun dismissTrackingRegDialog() {
        _uiState.value = _uiState.value.copy(showTrackingRegDialog = false)
    }

    fun confirmTrackingReg() {
        /* 대여 취소 로직 추가, 성공 시 닫기 */
        _uiState.value = _uiState.value.copy(showTrackingRegDialog = false)
    }

    fun navigateToPay() {
        viewModelScope.launch {
            _sideEffect.emit(RenterRentalDetailSideEffect.NavigateToPay)
        }
    }

    fun navigateToPhotoBeforeReturn() {
        viewModelScope.launch {
            _sideEffect.emit(RenterRentalDetailSideEffect.NavigateToPhotoBeforeReturn)
        }
    }

    fun navigateToRentalPhotoCheck() {
        viewModelScope.launch {
            _sideEffect.emit(RenterRentalDetailSideEffect.NavigateToRentalPhotoCheck)
        }
    }
}