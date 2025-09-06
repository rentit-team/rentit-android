package com.example.rentit.presentation.rentaldetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.RentalRole
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.enums.TrackingRegistrationRequestType
import com.example.rentit.common.exception.MissingTokenException
import com.example.rentit.common.uimodel.RequestAcceptDialogUiModel
import com.example.rentit.data.rental.dto.UpdateRentalStatusRequestDto
import com.example.rentit.data.rental.mapper.toUiModel
import com.example.rentit.domain.rental.repository.RentalRepository
import com.example.rentit.domain.rental.usecase.RegisterTrackingUseCase
import com.example.rentit.presentation.rentaldetail.model.RentalStatusUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RentalDetailViewModel @Inject constructor(
    private val rentalRepository: RentalRepository,
    private val registerTrackingUseCase: RegisterTrackingUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RentalDetailState())
    val uiState: StateFlow<RentalDetailState> = _uiState

    private val _sideEffect = MutableSharedFlow<RentalDetailSideEffect>()
    val sideEffect: SharedFlow<RentalDetailSideEffect> = _sideEffect

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRentalDetail(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            rentalRepository.getRentalDetail(productId, reservationId)
                .onSuccess {
                    val uiModel = it.toUiModel()
                    if (uiModel != RentalStatusUiModel.Unknown) {
                        _uiState.value = _uiState.value.copy(rentalStatusUiModel = uiModel)
                        // TODO: User의 Role 확인
                    } else {
                        showUnknownStatusDialog()
                    }
                }.onFailure {
                    showUnknownStatusDialog()
                }
        }
    }

    private fun showUnknownStatusDialog() {
        _uiState.value = _uiState.value.copy(showUnknownStatusDialog = true)
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    /** 요청 수락 */
    @RequiresApi(Build.VERSION_CODES.O)
    fun showRequestAcceptDialog() {
        val requestData =
            _uiState.value.rentalStatusUiModel as? RentalStatusUiModel.Request ?: return
        _uiState.value = _uiState.value.copy(
            requestAcceptDialog = RequestAcceptDialogUiModel(
                startDate = requestData.rentalSummary.startDate,
                endDate = requestData.rentalSummary.endDate,
                expectedRevenue = requestData.basicRentalFee
            )
        )
    }

    fun dismissRequestAcceptDialog() {
        _uiState.value = _uiState.value.copy(requestAcceptDialog = null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun acceptRequest(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            rentalRepository.updateRentalStatus(
                productId,
                reservationId,
                UpdateRentalStatusRequestDto(RentalStatus.ACCEPTED)
            ).onSuccess {
                toastAcceptSuccess()
                dismissRequestAcceptDialog()
                getRentalDetail(productId, reservationId)
            }.onFailure { e ->
                handleAcceptError(e)
            }
        }
    }

    private fun toastAcceptSuccess() {
        viewModelScope.launch {
            _sideEffect.emit(RentalDetailSideEffect.ToastAcceptRentalSuccess)
        }
    }

    private fun handleAcceptError(e: Throwable) {
        viewModelScope.launch {
            when (e) {
                is MissingTokenException -> println("Logout") // TODO: 로그아웃 및 로그인 이동
                else -> _sideEffect.emit(RentalDetailSideEffect.ToastAcceptRentalFailed)
            }
        }
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
            _sideEffect.emit(RentalDetailSideEffect.ToastCancelRentalSuccess)
        }
    }

    private fun handleCancelError(e: Throwable) {
        viewModelScope.launch {
            when (e) {
                is MissingTokenException -> println("Logout") // TODO: 로그아웃 및 로그인 이동
                else -> _sideEffect.emit(RentalDetailSideEffect.ToastCancelRentalFailed)
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
                    _sideEffect.emit(RentalDetailSideEffect.ToastErrorGetCourierNames)
                }
        }
    }

    fun showTrackingRegDialog() {
        if (_uiState.value.trackingCourierNames.isEmpty()) getCourierNames()
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
                type = TrackingRegistrationRequestType.RENTAL,
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
            _sideEffect.emit(RentalDetailSideEffect.ToastSuccessTrackingRegistration)
        }
    }

    private fun handleTrackingError(e: Throwable) {
        viewModelScope.launch {
            when (e) {
                is IllegalArgumentException -> _uiState.value =
                    _uiState.value.copy(showTrackingNumberEmptyError = true)

                else -> _sideEffect.emit(RentalDetailSideEffect.ToastErrorTrackingRegistration)
            }
        }
    }

    fun changeSelectedCourierName(name: String) {
        _uiState.value = _uiState.value.copy(selectedCourierName = name)
    }

    fun changeTrackingNumber(number: String) {
        val showError =
            if (number.isNotBlank()) false else _uiState.value.showTrackingNumberEmptyError
        _uiState.value =
            _uiState.value.copy(trackingNumber = number, showTrackingNumberEmptyError = showError)
    }

    /** 네비게이션 */
    fun navigateBack() {
        viewModelScope.launch {
            _sideEffect.emit(RentalDetailSideEffect.NavigateBack)
        }
    }

    fun navigateToPay() {
        viewModelScope.launch {
            _sideEffect.emit(RentalDetailSideEffect.NavigateToPay)
        }
    }

    fun navigateToPhotoBeforeRent() {
        viewModelScope.launch {
            _sideEffect.emit(RentalDetailSideEffect.NavigateToPhotoBeforeRent)
        }
    }

    fun navigateToPhotoBeforeReturn() {
        viewModelScope.launch {
            _sideEffect.emit(RentalDetailSideEffect.NavigateToPhotoBeforeReturn)
        }
    }

    fun navigateToRentalPhotoCheck() {
        viewModelScope.launch {
            _sideEffect.emit(RentalDetailSideEffect.NavigateToRentalPhotoCheck)
        }
    }
}