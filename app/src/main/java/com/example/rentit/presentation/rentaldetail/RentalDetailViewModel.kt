package com.example.rentit.presentation.rentaldetail

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.enums.RentalProcessType
import com.example.rentit.common.uimodel.RequestAcceptDialogUiModel
import com.example.rentit.data.rental.dto.UpdateRentalStatusRequestDto
import com.example.rentit.domain.chat.repository.ChatRepository
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

private const val TAG = "RentalDetailViewModel"

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class RentalDetailViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val rentalRepository: RentalRepository,
    private val getRentalDetailUseCase: GetRentalDetailUseCase,
    private val registerTrackingUseCase: RegisterTrackingUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RentalDetailState())
    val uiState: StateFlow<RentalDetailState> = _uiState

    private val _sideEffect = MutableSharedFlow<RentalDetailSideEffect>()
    val sideEffect: SharedFlow<RentalDetailSideEffect> = _sideEffect

    private var chatRoomId: String? = null

    private fun emitSideEffect(sideEffect: RentalDetailSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    fun resetDialogState() {
        _uiState.value = _uiState.value.copy(
            showCancelDialog = false,
            requestAcceptDialog = null,
            showTrackingRegDialog = false,
            showTrackingNumberEmptyError = false
        )
    }

    /** 대여 상세 정보 조회 */
    suspend fun getRentalDetail(productId: Int, reservationId: Int) {
        setLoading(true)
        getRentalDetailUseCase(productId, reservationId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    rentalDetailStatusModel = it.rentalDetailStatusModel,
                    role = it.role
                )
                chatRoomId = it.chatRoomId
                Log.i(TAG, "대여 상세 조회 성공: Product Id: $productId, Reservation Id: $reservationId")
            }.onFailure { e ->
                Log.e(TAG, "대여 상세 조회 실패", e)
                emitSideEffect(RentalDetailSideEffect.CommonError(e))
            }
        setLoading(false)
    }

    fun retryLoadRentalDetail(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            getRentalDetail(productId, reservationId)
        }
    }

    /** 요청 수락 */
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
                Log.i(TAG, "요청 수락 성공")
            }.onFailure { e ->
                Log.e(TAG, "요청 수락 실패", e)
                emitSideEffect(RentalDetailSideEffect.ToastAcceptRentalFailed)
            }
        }
    }

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

    private fun toastAcceptSuccess() {
        emitSideEffect(RentalDetailSideEffect.ToastAcceptRentalSuccess)
    }

    /** 대여 취소 */
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
                Log.i(TAG, "대여 취소 성공")
            }.onFailure { e ->
                Log.e(TAG, "대여 취소 실패", e)
                emitSideEffect(RentalDetailSideEffect.ToastCancelRentalFailed)
            }
        }
    }

    fun showCancelDialog() {
        _uiState.value = _uiState.value.copy(showCancelDialog = true)
    }

    fun dismissCancelDialog() {
        _uiState.value = _uiState.value.copy(showCancelDialog = false)
    }

    private fun toastCancelSuccess() {
        emitSideEffect(RentalDetailSideEffect.ToastCancelRentalSuccess)
    }

    /** 운송장 등록 - 택배사 조회 */
    private fun getCourierNames() {
        viewModelScope.launch {
            rentalRepository.getCourierNames()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        trackingCourierNames = it.courierNames,
                        selectedCourierName = it.courierNames.firstOrNull() ?: ""
                    )
                    Log.i(TAG, "택배사 조회 성공: ${it.courierNames.size}개")
                }.onFailure { e ->
                    Log.e(TAG, "택배사 조회 실패", e)
                    emitSideEffect(RentalDetailSideEffect.ToastErrorGetCourierNames)
                }
        }
    }

    /** 운송장 등록 */
    fun confirmTrackingReg(requestType: RentalProcessType, productId: Int, reservationId: Int) {
        viewModelScope.launch {
            registerTrackingUseCase(
                productId = productId,
                reservationId = reservationId,
                type = requestType,
                courierName = _uiState.value.selectedCourierName,
                trackingNumber = _uiState.value.trackingNumber
            ).onSuccess {
                dismissTrackingRegDialog()
                emitSideEffect(RentalDetailSideEffect.ToastSuccessTrackingRegistration)
                getRentalDetail(productId, reservationId)
                Log.i(TAG, "운송장 등록 성공: ${it?.tracking}")
            }.onFailure { e ->
                Log.e(TAG, "운송장 등록 실패", e)
                handleTrackingError(e)
            }
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

    fun showTrackingRegDialog() {
        if (_uiState.value.trackingCourierNames.isEmpty()) getCourierNames()
        _uiState.value = _uiState.value.copy(showTrackingRegDialog = true)
    }

    fun dismissTrackingRegDialog() {
        _uiState.value = _uiState.value.copy(showTrackingRegDialog = false)
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

    fun navigateToProductDetail() {
        emitSideEffect(RentalDetailSideEffect.NavigateToProductDetail)
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

    fun onChattingClicked(productId: Int) {
        if(chatRoomId == null) {
            viewModelScope.launch {
                chatRepository.postNewChat(productId)
                    .onSuccess {
                        chatRoomId = it.data.chatRoomId
                        chatRoomId?.let { id -> emitSideEffect(RentalDetailSideEffect.NavigateToChatRoom(id)) }
                    }.onFailure {
                        emitSideEffect(RentalDetailSideEffect.ToastChatRoomError)
                    }
            }
        } else {
            emitSideEffect(RentalDetailSideEffect.NavigateToChatRoom(chatRoomId!!))
        }
    }
}