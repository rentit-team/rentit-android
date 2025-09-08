package com.example.rentit.presentation.mypage

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.exception.MissingTokenException
import com.example.rentit.domain.user.repository.UserRepository
import com.example.rentit.domain.user.usecase.GetMyProductsWithCategoryUseCase
import com.example.rentit.domain.user.usecase.GetMyRentalsWithNearestDueUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val getMyRentalsWithNearestDueUseCase: GetMyRentalsWithNearestDueUseCase,
    private val getMyProductsWithCategoryUseCase: GetMyProductsWithCategoryUseCase
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

    private fun errorHandling(e: Throwable) {
        when(e) {
            is MissingTokenException -> {
                // TODO: 로그아웃 및 로그인 화면으로 이동
            }
            is IOException -> {
                showNetworkErrorDialog()
            }
            else -> {
                showServerErrorDialog()
            }
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    private suspend fun getMyProductList() {
        getMyProductsWithCategoryUseCase()
            .onSuccess {
                _uiState.value = _uiState.value.copy(myProductList = it)
            }.onFailure { e ->
                errorHandling(e)
            }
    }

    private suspend fun getMyRentalList() {
        getMyRentalsWithNearestDueUseCase()
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    myRentalList = it.myRentalList,
                    nearestDueItem = it.nearestDueItem
                )
            }.onFailure { e ->
                errorHandling(e)
            }
    }

    private suspend fun getMyInfo() {
        userRepository.getMyInfo().onSuccess {
            _uiState.value = _uiState.value.copy(
                profileImgUrl = it.data.profileImgUrl,
                nickName = it.data.nickname,
            )
        }.onFailure { e ->
            errorHandling(e)
        }
    }

    suspend fun loadInitialData() {
        setLoading(true)
        getMyInfo()
        getMyProductList()
        getMyRentalList()
        setLoading(false)
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

    fun showNetworkErrorDialog() {
        _uiState.value = _uiState.value.copy(showNetworkErrorDialog = true)
    }

    fun showServerErrorDialog() {
        _uiState.value = _uiState.value.copy(showServerErrorDialog = true)
    }

    fun reloadData() {
        dismissAllDialog()
        viewModelScope.launch {
            loadInitialData()
        }
    }

    fun dismissAllDialog() {
        _uiState.value = _uiState.value.copy(
            showNetworkErrorDialog = false,
            showServerErrorDialog = false
        )
    }
}