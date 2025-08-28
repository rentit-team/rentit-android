package com.example.rentit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.usecase.GetProductListWithCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductListWithCategoryUseCase: GetProductListWithCategoryUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    init {
        loadProductList()
    }

    private fun loadProductList() {
        viewModelScope.launch {
            setIsLoading(true)
            getProductListWithCategoryUseCase.invoke()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(productList = it)
                }
                .onFailure { e ->
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
            setIsLoading(false)
        }
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

    fun retryFetchProductList(){
        _uiState.value = _uiState.value.copy(showServerErrorDialog = false, showNetworkErrorDialog = false)
        loadProductList()
    }
}