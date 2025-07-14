package com.example.rentit.presentation.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.storage.getMyIdFromPrefs
import com.example.rentit.common.storage.saveMyIdToPrefs
import com.example.rentit.data.product.dto.ProductListResponseDto
import com.example.rentit.data.product.repository.ProductRepository
import com.example.rentit.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _productList = MutableStateFlow<Result<ProductListResponseDto>?>(null)
    val productList: StateFlow<Result<ProductListResponseDto>?> = _productList
    private val myId = getMyIdFromPrefs(context)

    init {
        if(myId < 0) getMyId()
    }

    fun getProductList() {
        viewModelScope.launch {
            _productList.value = productRepository.getProductList()
        }
    }

    private fun getMyId() {
        viewModelScope.launch {
            userRepository.getMyInfo().onSuccess {
                saveMyIdToPrefs(context, it.data.userId)
            }.onFailure {
                /* 사용자 정보 가져오기 실패 시*/
            }
        }
    }
}