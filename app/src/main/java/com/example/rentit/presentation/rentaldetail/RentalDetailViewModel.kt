package com.example.rentit.presentation.rentaldetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.enums.TrackingRegistrationRequestType
import com.example.rentit.common.exception.MissingTokenException
import com.example.rentit.common.uimodel.RequestAcceptDialogUiModel
import com.example.rentit.data.rental.dto.UpdateRentalStatusRequestDto
import com.example.rentit.domain.rental.exception.RentalStatusUnknownException
import com.example.rentit.domain.rental.usecase.RegisterTrackingUseCase
import com.example.rentit.domain.rental.model.RentalDetailStatusModel
import com.example.rentit.domain.rental.repository.RentalRepository
import com.example.rentit.domain.rental.usecase.GetRentalDetailUseCase
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
    private val getRentalDetailUseCase: GetRentalDetailUseCase,
    private val registerTrackingUseCase: RegisterTrackingUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RentalDetailState())
    val uiState: StateFlow<RentalDetailState> = _uiState

    private val _sideEffect = MutableSharedFlow<RentalDetailSideEffect>()
    val sideEffect: SharedFlow<RentalDetailSideEffect> = _sideEffect

    private fun emitSideEffect(sideEffect: RentalDetailSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRentalDetail(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            getRentalDetailUseCase(productId, reservationId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        rentalDetailStatusModel = it.rentalDetailStatusModel,
                        role = it.role
                    )
                }.onFailure { e ->
                    when(e) {
                        is RentalStatusUnknownException -> {
                            showUnknownStatusDialog()
                        }
                        else -> {
                            showLoadFailedDialog()
                        }
                    }
                }
        }
    }

    private fun showUnknownStatusDialog() {
        _uiState.value = _uiState.value.copy(showUnknownStatusDialog = true)
    }

    private fun showLoadFailedDialog() {
        _uiState.value = _uiState.value.copy(showLoadFailedDialog = true)
    }

    /** 요청 수락 */
    @RequiresApi(Build.VERSION_CODES.O)
    fun showRequestAcceptDialog() {
        val requestData =
            _uiState.value.rentalDetailStatusModel as? RentalDetailStatusModel.Request ?: return
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
        emitSideEffect(RentalDetailSideEffect.ToastAcceptRentalSuccess)
    }

    private fun handleAcceptError(e: Throwable) {
        when (e) {
            is MissingTokenException -> {
                println("Logout") // TODO: 로그아웃 및 로그인 이동
            }
            else -> {
                emitSideEffect(RentalDetailSideEffect.ToastAcceptRentalFailed)
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
        emitSideEffect(RentalDetailSideEffect.ToastCancelRentalSuccess)
    }

    private fun handleCancelError(e: Throwable) {
        when (e) {
            is MissingTokenException -> {
                println("Logout") // TODO: 로그아웃 및 로그인 이동
            }
            else -> {
                emitSideEffect(RentalDetailSideEffect.ToastCancelRentalFailed)
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
                    emitSideEffect(RentalDetailSideEffect.ToastErrorGetCourierNames)
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
                emitSideEffect(RentalDetailSideEffect.ToastSuccessTrackingRegistration)
                getRentalDetail(productId, reservationId)
            }.onFailure { e -> handleTrackingError(e) }
        }
    }

    private fun handleTrackingError(e: Throwable) {
        when (e) {
            is IllegalArgumentException -> {
                _uiState.value = _uiState.value.copy(showTrackingNumberEmptyError = true)
            }
            else -> {
                emitSideEffect(RentalDetailSideEffect.ToastErrorTrackingRegistration)
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
        emitSideEffect(RentalDetailSideEffect.NavigateBack)
    }

    fun navigateToPay() {
        emitSideEffect(RentalDetailSideEffect.NavigateToPay)
    }

    fun navigateToPhotoBeforeRent() {
        emitSideEffect(RentalDetailSideEffect.NavigateToPhotoBeforeRent)
    }

    fun navigateToPhotoBeforeReturn() {
        emitSideEffect(RentalDetailSideEffect.NavigateToPhotoBeforeReturn)
    }

    fun navigateToRentalPhotoCheck() {
        emitSideEffect(RentalDetailSideEffect.NavigateToRentalPhotoCheck)
    }
}