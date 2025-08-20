package com.example.rentit.presentation.rentaldetail.owner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.model.RequestAcceptDialogUiModel
import com.example.rentit.data.rental.repository.RentalRepository
import com.example.rentit.presentation.rentaldetail.owner.stateui.OwnerRentalStatusUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerRentalDetailViewModel @Inject constructor(
    private val rentalRepository: RentalRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnerRentalDetailState())
    val uiState: StateFlow<OwnerRentalDetailState> = _uiState

    private val _sideEffect = MutableSharedFlow<OwnerRentalDetailSideEffect>()
    val sideEffect: SharedFlow<OwnerRentalDetailSideEffect> = _sideEffect

    @RequiresApi(Build.VERSION_CODES.O)
    private val _rentalDetailUiModel = MutableStateFlow<OwnerRentalStatusUiModel>(
        OwnerRentalStatusUiModel.Unknown)
    @RequiresApi(Build.VERSION_CODES.O)
    val rentalDetailUiModel: StateFlow<OwnerRentalStatusUiModel> = _rentalDetailUiModel

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRentalDetail(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            rentalRepository.getRentalDetail(productId, reservationId)
                .onSuccess {
                    if(it.toOwnerUiModel() != OwnerRentalStatusUiModel.Unknown){
                        _rentalDetailUiModel.value = it.toOwnerUiModel()
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

    /** 요청 수락 */
    @RequiresApi(Build.VERSION_CODES.O)
    fun showRequestAcceptDialog() {
        val requestData = _rentalDetailUiModel.value as? OwnerRentalStatusUiModel.Request ?: return

        _uiState.value = _uiState.value.copy(
            requestAcceptDialog = RequestAcceptDialogUiModel(
                startDate = requestData.rentalSummary.startDate,
                endDate = requestData.rentalSummary.endDate,
                expectedRevenue = requestData.basicRentalFee
            ),
        )
    }

    fun dismissRequestAcceptDialog() {
        _uiState.value = _uiState.value.copy(requestAcceptDialog = null)
    }

    fun acceptRequest() {
        /* 요청 수락 로직 추가, 성공 시 닫기 */
        _uiState.value = _uiState.value.copy(requestAcceptDialog = null)
    }

    /** 대여 취소 */
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

    /** 운송장 등록 */
    fun getCourierNames() {
        viewModelScope.launch {
            rentalRepository.getCourierNames()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(trackingRegDialog = it.courierNames, selectedCourierName = it.courierNames.firstOrNull())
                }.onFailure {
                    _sideEffect.emit(OwnerRentalDetailSideEffect.ToastErrorGetCourierNames)
                }
        }
    }

    fun dismissTrackingRegDialog() {
        _uiState.value = _uiState.value.copy(trackingRegDialog = emptyList())
    }

    fun confirmTrackingReg() {
        /* 운송장 등록 로직 추가, 성공 시 닫기 */
        _uiState.value = _uiState.value.copy(trackingRegDialog = emptyList())
    }

    fun changeSelectedCourierName(name: String) {
        _uiState.value = _uiState.value.copy(selectedCourierName = name)
    }

    fun changeTrackingNumber(number: String) {
        _uiState.value = _uiState.value.copy(trackingNumber = number)
    }

    /** 네비게이션 */
    fun navigateToPhotoBeforeRent() {
        viewModelScope.launch {
            _sideEffect.emit(OwnerRentalDetailSideEffect.NavigateToPhotoBeforeRent)
        }
    }

    fun navigateToRentalPhotoCheck() {
        viewModelScope.launch {
            _sideEffect.emit(OwnerRentalDetailSideEffect.NavigateToRentalPhotoCheck)
        }
    }
}