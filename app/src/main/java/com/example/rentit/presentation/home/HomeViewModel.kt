package com.example.rentit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.usecase.GetCategoryMapUseCase
import com.example.rentit.data.product.usecase.GetProductListWithCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoryMapUseCase: GetCategoryMapUseCase,
    private val getProductListWithCategoryUseCase: GetProductListWithCategoryUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    init {
        fetchHomeData()
    }

    private fun fetchHomeData() {
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
            }
            .onFailure { e -> handleException(e) }
    }

    private suspend fun fetchProductList() {
        getProductListWithCategoryUseCase.invoke(_uiState.value.categoryMap)
            .onSuccess {
                _uiState.value = _uiState.value.copy(productList = it)
            }
            .onFailure { e -> handleException(e) }
    }

    fun toggleOnlyRentalAvailable() {
        val newValue = !_uiState.value.onlyRentalAvailable
        _uiState.value = _uiState.value.copy(onlyRentalAvailable = newValue)
    }

    fun filterByParentCategory(parentCategoryId: Int) {
        _uiState.value = _uiState.value.copy(filterParentCategoryId = parentCategoryId)
    }

    fun retryFetchProductList(){
        _uiState.value = _uiState.value.copy(showServerErrorDialog = false, showNetworkErrorDialog = false)
        fetchHomeData()
    }

    private fun setIsLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    private fun showServerErrorDialog() {
        _uiState.value = _uiState.value.copy(showServerErrorDialog = true)
    }

    private fun showNetworkErrorDialog() {
        _uiState.value = _uiState.value.copy(showNetworkErrorDialog = true)
    }

    private fun handleException(e: Throwable) {
        when (e) {
            is IOException -> {
                showNetworkErrorDialog()
            }
            else -> {
                showServerErrorDialog()
            }
            // TODO: 토큰 에러 처리 필요 (리프레시 토큰으로 재발급 또는 로그인 화면 이동)
        }
    }
}