package com.example.rentit.presentation.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.domain.user.repository.UserRepository
import com.example.rentit.domain.user.usecase.GetMyProductsWithCategoryUseCase
import com.example.rentit.domain.user.usecase.GetMyRentalsWithNearestDueUseCase
import com.example.rentit.presentation.mypage.model.MyPageTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MyPageViewModel"

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
                Log.i(TAG, "내 상품 조회 성공: ${it.size}개")
            }.onFailure { e ->
                Log.e(TAG, "내 상품 조회 실패", e)
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
                Log.i(TAG, "내 대여 조회 성공: ${it.myRentalList.size}개")
            }.onFailure { e ->
                Log.e(TAG, "내 대여 조회 실패", e)
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
                Log.i(TAG, "내 상품에 대한 대여 조회 성공: ${it.rentalHistory.size}개")
            }.onFailure { e ->
                Log.e(TAG, "내 상품에 대한 대여 조회 실패", e)
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

    fun onMyProductCountClicked() {
        _uiState.value = _uiState.value.copy(currentTab = MyPageTab.MY_PRODUCT)
    }

    fun onMyRentingCountClicked() {
        _uiState.value = _uiState.value.copy(currentTab = MyPageTab.MY_RENTAL)
    }

    fun onMyPendingRentalClicked() {
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

    fun setTabSelected(selectedTab: MyPageTab) {
        _uiState.value = _uiState.value.copy(currentTab = selectedTab)
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