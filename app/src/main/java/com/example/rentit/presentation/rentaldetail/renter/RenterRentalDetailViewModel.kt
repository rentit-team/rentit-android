package com.example.rentit.presentation.rentaldetail.renter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.enums.TrackingRegistrationRequestType
import com.example.rentit.common.exception.MissingTokenException
import com.example.rentit.data.rental.dto.UpdateRentalStatusRequestDto
import com.example.rentit.data.rental.repository.RentalRepository
import com.example.rentit.data.rental.usecase.RegisterTrackingUseCase
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
    private val registerTrackingUseCase: RegisterTrackingUseCase,
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

    /** 대여 취소 */
    fun showCancelDialog() {
        _uiState.value = _uiState.value.copy(showCancelDialog = true)
    }

    fun dismissCancelDialog() {
        _uiState.value = _uiState.value.copy(showCancelDialog = false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun confirmCancel(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            rentalRepository.updateRentalStatus(
                productId,
                reservationId,
                UpdateRentalStatusRequestDto(RentalStatus.CANCELED)
            ).onSuccess {
                toastCancelSuccess()
                dismissCancelDialog()
                getRentalDetail(productId, reservationId)
            }.onFailure { e ->
                handleCancelError(e)
            }
        }
    }

    private fun toastCancelSuccess() {
        viewModelScope.launch {
            _sideEffect.emit(RenterRentalDetailSideEffect.ToastCancelRentalSuccess)
        }
    }

    private fun handleCancelError(e: Throwable) {
        viewModelScope.launch {
            when(e) {
                is MissingTokenException -> println("Logout") /* 로그아웃 수행 */
                else -> _sideEffect.emit(RenterRentalDetailSideEffect.ToastCancelRentalFailed)
            }
        }
    }

    /** 운송장 등록 */
    private fun getCourierNames() {
        viewModelScope.launch {
            rentalRepository.getCourierNames()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        trackingCourierNames = it.courierNames,
                        selectedCourierName = it.courierNames.firstOrNull() ?: ""
                    )
                }.onFailure {
                    _sideEffect.emit(RenterRentalDetailSideEffect.ToastErrorGetCourierNames)
                }
        }
    }

    fun showTrackingRegDialog() {
        if(_uiState.value.trackingCourierNames.isEmpty()) getCourierNames()
        _uiState.value = _uiState.value.copy(showTrackingRegDialog = true)
    }

    fun dismissTrackingRegDialog() {
        _uiState.value = _uiState.value.copy(showTrackingRegDialog = false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun confirmTrackingReg(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            registerTrackingUseCase(
                productId = productId,
                reservationId = reservationId,
                type = TrackingRegistrationRequestType.RETURN,
                courierName = _uiState.value.selectedCourierName,
                trackingNumber = _uiState.value.trackingNumber
            ).onSuccess {
                dismissTrackingRegDialog()
                toastTrackingRegistered()
                getRentalDetail(productId, reservationId)
            }.onFailure { e -> handleTrackingError(e) }
        }
    }

    private fun toastTrackingRegistered() {
        viewModelScope.launch {
            _sideEffect.emit(RenterRentalDetailSideEffect.ToastSuccessTrackingRegistration)
        }
    }

    private fun handleTrackingError(e: Throwable) {
        viewModelScope.launch {
            when (e) {
                is IllegalArgumentException -> _uiState.value = _uiState.value.copy(showTrackingNumberEmptyError = true)
                else -> _sideEffect.emit(RenterRentalDetailSideEffect.ToastErrorTrackingRegistration)
            }
        }
    }

    fun changeSelectedCourierName(name: String) {
        _uiState.value = _uiState.value.copy(selectedCourierName = name)
    }

    fun changeTrackingNumber(number: String) {
        val showError = if(number.isNotBlank()) false else _uiState.value.showTrackingNumberEmptyError
        _uiState.value = _uiState.value.copy(trackingNumber = number, showTrackingNumberEmptyError = showError)
    }

    /** 네비게이션 */
    fun navigateBack() {
        viewModelScope.launch {
            _sideEffect.emit(RenterRentalDetailSideEffect.NavigateBack)
        }
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