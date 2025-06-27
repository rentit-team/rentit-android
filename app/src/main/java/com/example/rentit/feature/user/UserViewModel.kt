package com.example.rentit.feature.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.chat.repository.ChatRepository
import com.example.rentit.data.product.dto.ProductDto
import com.example.rentit.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val _myProductList = MutableStateFlow<List<ProductDto>>(emptyList())
    val myProductList: StateFlow<List<ProductDto>> = _myProductList

    fun getMyProductList() {
        viewModelScope.launch {
            userRepository.getMyProductList().onSuccess {
                _myProductList.value = it.myProducts
            }.onFailure {
                /* 로딩 실패 시 */
            }
        }
    }

    fun postNewChat(productId: Int, onSuccess: (String) -> Unit = {}, onError: (Throwable) -> Unit = {}) {
        viewModelScope.launch {
            chatRepository.postNewChat(productId)
                .onSuccess {
                    onSuccess(it.data.chatRoomId)
                }
                .onFailure(onError)
        }
    }
}