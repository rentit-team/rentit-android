package com.example.rentit.presentation.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.domain.product.usecase.GetProductDetailResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailResultUseCase: GetProductDetailResultUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailState())
    val uiState: StateFlow<ProductDetailState> = _uiState

    private val _sideEffect = MutableSharedFlow<ProductDetailSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private fun requestNavigation(sideEffect: ProductDetailSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    suspend fun getProductDetail(productId: Int) {
        getProductDetailResultUseCase.invoke(productId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    productDetail = it.productDetail,
                    requestCount = it.requestCount
                )
            }.onFailure { }
    }

    fun showBottomSheet() {
        _uiState.value = _uiState.value.copy(showBottomSheet = true)
    }

    fun showFullImage() {
        _uiState.value = _uiState.value.copy(showFullImage = true)
    }

    fun hideFullImage() {
        _uiState.value = _uiState.value.copy(showFullImage = false)
    }

    fun onChattingClicked() {
        requestNavigation(ProductDetailSideEffect.NavigateToChatting)
    }

    fun onResvRequestClicked() {
        requestNavigation(ProductDetailSideEffect.NavigateToResvRequest)
    }

    fun onRentalHistoryClicked() {
        requestNavigation(ProductDetailSideEffect.NavigateToRentalHistory)
    }

    fun emitComingSoonToast() {
        viewModelScope.launch {
            _sideEffect.emit(ProductDetailSideEffect.ToastComingSoon)
        }
    }
}