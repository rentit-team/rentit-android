package com.example.rentit.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.domain.product.usecase.GetCategoryMapUseCase
import com.example.rentit.domain.product.usecase.GetProductListWithCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoryMapUseCase: GetCategoryMapUseCase,
    private val getProductListWithCategoryUseCase: GetProductListWithCategoryUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    private val _sideEffect = MutableSharedFlow<HomeSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private fun emitSideEffect(sideEffect: HomeSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            setIsLoading(true)
            fetchCategoryMap()
            fetchProductList()
            setIsLoading(false)
        }
    }

    private suspend fun fetchCategoryMap() {
        getCategoryMapUseCase.invoke()
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    categoryMap = it,
                    parentIdToNameCategoryMap = linkedMapOf(-1 to "") + it.filter { cate -> cate.value.isParent }
                        .mapValues { cate -> cate.value.name }
                )
                Log.i(TAG, "카테고리 맵 가져오기 성공: ${it.size}개 카테고리")
            }
            .onFailure { e ->
                Log.e(TAG, "카테고리 맵 가져오기 실패", e)
                emitSideEffect(HomeSideEffect.CommonError(e))
            }
    }

    private suspend fun fetchProductList() {
        getProductListWithCategoryUseCase.invoke(_uiState.value.categoryMap)
            .onSuccess {
                _uiState.value = _uiState.value.copy(productList = it)
                Log.i(TAG, "상품 리스트 가져오기 성공: ${it.size}개 상품")
            }
            .onFailure { e ->
                Log.e(TAG, "상품 리스트 가져오기 실패", e)
                emitSideEffect(HomeSideEffect.CommonError(e))
            }
    }

    fun toggleOnlyRentalAvailable() {
        val newValue = !_uiState.value.onlyRentalAvailable
        _uiState.value = _uiState.value.copy(onlyRentalAvailable = newValue)
    }

    fun filterByParentCategory(parentCategoryId: Int) {
        _uiState.value = _uiState.value.copy(filterParentCategoryId = parentCategoryId)
        emitSideEffect(HomeSideEffect.ScrollToTop)
    }

    fun retryFetchProductList(){
        _uiState.value = _uiState.value.copy(showServerErrorDialog = false, showNetworkErrorDialog = false)
        fetchHomeData()
    }

    private fun setIsLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }
}