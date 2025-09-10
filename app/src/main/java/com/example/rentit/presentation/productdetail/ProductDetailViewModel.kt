package com.example.rentit.presentation.productdetail

import android.util.Log
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

private const val TAG = "ProductDetailViewModel"

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
                    isUserOwner = it.isUserOwner,
                    requestCount = it.requestCount
                )
                Log.i(TAG, "상품 상세 정보 조회 성공: Product Id: $productId")
            }.onFailure { e ->
                Log.e(TAG, "상품 상세 정보 조회 실패", e)
                emitSideEffect(ProductDetailSideEffect.CommonError(e))
            }
    }

    private suspend fun getReservedDates(productId: Int) {
        productRepository.getReservedDates(productId)
            .onSuccess {
                _uiState.value = _uiState.value.copy(reservedDateList = it.disabledDates)
                Log.i(TAG, "상품 예약일 조회 성공: 예약된 날짜 ${it.disabledDates.size}개")
            }.onFailure {e ->
                Log.e(TAG, "상품 예약일 조회 실패", e)
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
                    Log.i(TAG, "채팅 접근 가능 여부 조회 성공: 채팅 접근 가능 여부 ${it.canAccessChat}")
                }.onFailure {e ->
                    Log.e(TAG, "채팅 접근 가능 여부 조회 실패", e)
                    emitSideEffect(ProductDetailSideEffect.CommonError(e))
                }
        }
    }

    fun showMenuDrawer() {
        _uiState.value = _uiState.value.copy(showMenuDrawer = true)
    }

    fun hideMenuDrawer() {
        _uiState.value = _uiState.value.copy(showMenuDrawer = false)
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