package com.example.rentit.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.dto.ProductListResponseDto
import com.example.rentit.data.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _productList = MutableStateFlow<Result<ProductListResponseDto>?>(null)
    val productList: StateFlow<Result<ProductListResponseDto>?> = _productList

    fun getProductList() {
        viewModelScope.launch {
            _productList.value = repository.getProductList()
        }
    }
}