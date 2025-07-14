package com.example.rentit.presentation.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.ResvStatus
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.dto.RequestInfoDto
import com.example.rentit.data.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _productDetail = MutableStateFlow<Result<ProductDetailResponseDto>?>(null)
    val productDetail: StateFlow<Result<ProductDetailResponseDto>?> = _productDetail

    private val _requestList =  MutableStateFlow<List<RequestInfoDto>>(emptyList())
    val requestList: StateFlow<List<RequestInfoDto>> = _requestList

    fun getProductDetail(productId: Int) {
        viewModelScope.launch {
            _productDetail.value = repository.getProductDetail(productId)
        }
    }

    fun getProductRequestList(productId: Int){
        viewModelScope.launch {
            repository.getProductRequestList(productId).onSuccess {
                _requestList.value = it.reservations.filter { data -> ResvStatus.isPending(data.status) }
            }
        }
    }
}