package com.example.rentit.presentation.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.ResvStatus
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.dto.RequestInfoDto
import com.example.rentit.data.product.repository.ProductRepository
import com.example.rentit.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _productDetail = MutableStateFlow<Result<ProductDetailResponseDto>?>(null)
    val productDetail: StateFlow<Result<ProductDetailResponseDto>?> = _productDetail

    private val _requestList =  MutableStateFlow<List<RequestInfoDto>>(emptyList())
    val requestList: StateFlow<List<RequestInfoDto>> = _requestList

    fun getAuthUserIdFromPrefs(): Long = userRepository.getAuthUserIdFromPrefs()

    fun getProductDetail(productId: Int) {
        viewModelScope.launch {
            _productDetail.value = productRepository.getProductDetail(productId)
        }
    }

    fun getProductRequestList(productId: Int){
        viewModelScope.launch {
            productRepository.getProductRequestList(productId).onSuccess {
                _requestList.value = it.reservations.filter { data -> ResvStatus.isPending(data.status) }
            }
        }
    }
}