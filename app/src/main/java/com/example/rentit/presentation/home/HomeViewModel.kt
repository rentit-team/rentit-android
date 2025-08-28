package com.example.rentit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.dto.ProductDto
import com.example.rentit.data.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : ViewModel() {

    private val _productList = MutableStateFlow<List<ProductDto>>(emptyList())
    val productList: StateFlow<List<ProductDto>> = _productList

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    init {
        setIsLoading(true)
        fetchProductList()
        setIsLoading(false)
    }

    private fun fetchProductList() {
        viewModelScope.launch {
            productRepository.getProductList()
                .onSuccess {
                    _productList.value = it.products
                }.onFailure { e ->
                    when(e) {
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
        viewModelScope.launch {
            fetchProductList()
        }
    }
}