package com.example.rentit.presentation.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.domain.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyPageState())
    val uiState: StateFlow<MyPageState> = _uiState

    private val _sideEffect = MutableSharedFlow<MyPageSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private fun emitSideEffect(sideEffect: MyPageSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    fun getMyProductList() {
        viewModelScope.launch {
            userRepository.getMyProductList().onSuccess {
                _uiState.value = _uiState.value.copy(myProductList = it.myProducts)
            }.onFailure {
                /* 로딩 실패 시 */
            }
        }
    }

    fun getMyRentalList() {
        viewModelScope.launch {
            userRepository.getMyRentalList().onSuccess {
                _uiState.value = _uiState.value.copy(myRentalList = it.myReservations)
            }.onFailure {
                /* 로딩 실패 시 */
            }
        }
    }

    fun onProductItemClicked(productId: Int) {
        emitSideEffect(MyPageSideEffect.NavigateToProductDetail(productId))
    }

    fun onRentalItemClicked(productId: Int, reservationId: Int) {
        emitSideEffect(MyPageSideEffect.NavigateToRentalDetail(productId, reservationId))
    }

    fun onSettingClicked() {
        emitSideEffect(MyPageSideEffect.NavigateToSetting)
    }

    fun onInfoRentalDetailClicked() {
        // TODO: API 수정 후 productId, reservationId 값 변경
        emitSideEffect(MyPageSideEffect.NavigateToRentalDetail(0, 0))
    }

    fun setTabSelected() {
        _uiState.value = _uiState.value.copy(isFirstTabSelected = !uiState.value.isFirstTabSelected)
    }

    fun showComingSoonMessage() {
        emitSideEffect(MyPageSideEffect.ToastComingSoon)
    }
}