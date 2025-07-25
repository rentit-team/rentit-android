package com.example.rentit.presentation.productdetail.reservation.requesthistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.ResvStatus
import com.example.rentit.data.chat.repository.ChatRepository
import com.example.rentit.data.product.dto.RequestInfoDto
import com.example.rentit.data.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestHistoryViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val chatRepository: ChatRepository
): ViewModel() {

    private val _requestList =  MutableStateFlow<List<RequestInfoDto>>(emptyList())
    val requestList: StateFlow<List<RequestInfoDto>> = _requestList

    fun getProductRequestList(productId: Int){
        viewModelScope.launch {
            productRepository.getProductRequestList(productId).onSuccess {
                _requestList.value = it.reservations.filter { data -> ResvStatus.isPending(data.status) }
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