package com.example.rentit.feature.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.dto.ProductDto
import com.example.rentit.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _myProductList = MutableStateFlow<List<ProductDto>>(emptyList())
    val myProductList: StateFlow<List<ProductDto>> = _myProductList

    fun getMyProductList() {
        viewModelScope.launch {
            repository.getMyProductList().onSuccess {
                _myProductList.value = it.myProducts
            }.onFailure {
                /* 로딩 실패 시 */
            }
        }
    }

}