package com.example.rentit.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.dto.ProductListResponseDto
import com.example.rentit.data.product.repository.ProductRepository
import com.example.rentit.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _productList = MutableStateFlow<Result<ProductListResponseDto>?>(null)
    val productList: StateFlow<Result<ProductListResponseDto>?> = _productList
    private val authUserId = userRepository.getAuthUserIdFromPrefs()

    init {
        if(authUserId < 0) getMyId()
    }

    fun getProductList() {
        viewModelScope.launch {
            _productList.value = productRepository.getProductList()
        }
    }

    private fun getMyId() {
        viewModelScope.launch {
            userRepository.getMyInfo().onSuccess {
                userRepository.saveAuthUserIdToPrefs(it.data.userId)
            }.onFailure {
                /* 사용자 정보 가져오기 실패 시*/
            }
        }
    }
}