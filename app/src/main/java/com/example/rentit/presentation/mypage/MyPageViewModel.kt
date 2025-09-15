package com.example.rentit.presentation.mypage

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.domain.user.repository.UserRepository
import com.example.rentit.domain.user.usecase.GetMyProductsWithCategoryUseCase
import com.example.rentit.domain.user.usecase.GetMyRentalsWithNearestDueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getMyRentalsWithNearestDueUseCase: GetMyRentalsWithNearestDueUseCase,
    private val getMyProductsWithCategoryUseCase: GetMyProductsWithCategoryUseCase,
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

    private fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    private suspend fun getMyProductList() {
        getMyProductsWithCategoryUseCase()
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    myProductList = it,
                    myProductCount = it.size
                )
            }.onFailure { e ->
                emitSideEffect(MyPageSideEffect.CommonError(e))
            }
    }

    private suspend fun getMyRentalList() {
        getMyRentalsWithNearestDueUseCase()
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    myRentalList = it.myRentalList,
                    myValidRentalCount = it.myValidRentalCount,
                    nearestDueItem = it.nearestDueItem
                )
            }.onFailure { e ->
                Log.e("MyPageViewModel", "getMyRentalList: $e")
                emitSideEffect(MyPageSideEffect.CommonError(e))
            }
    }

    private suspend fun getMyPendingRentalCount() {
        userRepository.getMyProductsRentalList()
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    myPendingRentalCount = it.rentalHistory
                        .filter { history -> history.status == RentalStatus.PENDING || history.status == RentalStatus.PAID }.size
                )
            }.onFailure { e ->
                emitSideEffect(MyPageSideEffect.CommonError(e))
            }
    }

    private suspend fun getMyInfo() {
        userRepository.getMyInfo().onSuccess {
            _uiState.value = _uiState.value.copy(
                profileImgUrl = it.data.profileImgUrl,
                nickName = it.data.nickname,
            )
        }.onFailure { e ->
            emitSideEffect(MyPageSideEffect.CommonError(e))
        }
    }

    suspend fun loadInitialData() {
        setLoading(true)
        getMyInfo()
        getMyProductList()
        getMyRentalList()
        getMyPendingRentalCount()
        setLoading(false)
    }

    fun onProductItemClicked(productId: Int) {
        emitSideEffect(MyPageSideEffect.NavigateToProductDetail(productId))
    }

    fun onRentalItemClicked(productId: Int, reservationId: Int) {
        emitSideEffect(MyPageSideEffect.NavigateToRentalDetail(productId, reservationId))
    }

    fun onPendingRentalClicked() {
        emitSideEffect(MyPageSideEffect.NavigateToMyProductsRental)
    }

    fun onSettingClicked() {
        emitSideEffect(MyPageSideEffect.NavigateToSetting)
    }

    fun onInfoRentalDetailClicked() {
        val myInfoModel = _uiState.value.nearestDueItem
        myInfoModel?.let {
            emitSideEffect(MyPageSideEffect.NavigateToRentalDetail(myInfoModel.productId, myInfoModel.reservationId))
        }
    }

    fun setTabSelected() {
        _uiState.value = _uiState.value.copy(isFirstTabSelected = !uiState.value.isFirstTabSelected)
    }

    fun showComingSoonMessage() {
        emitSideEffect(MyPageSideEffect.ToastComingSoon)
    }

    fun reloadData() {
        viewModelScope.launch {
            loadInitialData()
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true)
            loadInitialData()
            _uiState.value = _uiState.value.copy(isRefreshing = false)
        }
    }
}