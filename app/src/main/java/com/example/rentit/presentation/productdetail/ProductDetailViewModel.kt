package com.example.rentit.presentation.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.domain.product.repository.ProductRepository
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
    private val productRepository: ProductRepository,
    private val getProductDetailResultUseCase: GetProductDetailResultUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductDetailState())
    val uiState: StateFlow<ProductDetailState> = _uiState

    private val _sideEffect = MutableSharedFlow<ProductDetailSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private fun emitSideEffect (sideEffect: ProductDetailSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(sideEffect)
        }
    }

    private fun setLoading(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }

    suspend fun loadProductDetail(productId: Int) {
        setLoading(true)
        getProductDetail(productId)
        getReservedDates(productId)
        setLoading(false)
    }

    private suspend fun getProductDetail(productId: Int) {
        getProductDetailResultUseCase.invoke(productId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(
                    productDetail = it.productDetail,
                    requestCount = it.requestCount
                )
            }.onFailure { e ->
                emitSideEffect(ProductDetailSideEffect.CommonError(e))
            }
    }

    private suspend fun getReservedDates(productId: Int) {
        productRepository.getReservedDates(productId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(reservedDateList = it.disabledDates)
            }.onFailure {e ->
                emitSideEffect(ProductDetailSideEffect.CommonError(e))
            }
    }

    fun onChattingClicked(productId: Int) {
        viewModelScope.launch {
            productRepository.getChatAccessibility(productId)
                .onSuccess {
                    if(it.canAccessChat) {
                        emitSideEffect(ProductDetailSideEffect.NavigateToChatting(it.chatroomId))
                    } else {
                        showChatUnavailableDialog()
                    }
                }.onFailure {e ->
                    emitSideEffect(ProductDetailSideEffect.CommonError(e))
                }
        }
    }

    fun showBottomSheet() {
        _uiState.value = _uiState.value.copy(showBottomSheet = true)
    }

    fun hideBottomSheet() {
        _uiState.value = _uiState.value.copy(showBottomSheet = false)
    }

    fun showFullImage() {
        _uiState.value = _uiState.value.copy(showFullImage = true)
    }

    fun hideFullImage() {
        _uiState.value = _uiState.value.copy(showFullImage = false)
    }

    private fun showChatUnavailableDialog() {
        _uiState.value = _uiState.value.copy(showChatUnavailableDialog = true)
    }

    fun hideChatUnavailableDialog() {
        _uiState.value = _uiState.value.copy(showChatUnavailableDialog = false)
    }

    fun onResvRequestClicked() {
        emitSideEffect(ProductDetailSideEffect.NavigateToResvRequest)
    }

    fun onRentalHistoryClicked() {
        emitSideEffect(ProductDetailSideEffect.NavigateToRentalHistory)
    }

    fun emitComingSoonToast() {
        viewModelScope.launch {
            _sideEffect.emit(ProductDetailSideEffect.ToastComingSoon)
        }
    }

    fun retryLoadProductDetail(productId: Int) {
        viewModelScope.launch {
            loadProductDetail(productId)
        }
    }
}