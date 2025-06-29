package com.example.rentit.feature.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.chat.dto.ChatMessageDto
import com.example.rentit.data.chat.dto.ChatRoomSummaryDto
import com.example.rentit.data.chat.repository.ChatRepository
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _chatList = MutableStateFlow<List<ChatRoomSummaryDto>>(emptyList())
    val chatList: StateFlow<List<ChatRoomSummaryDto>> = _chatList

    private val _productDetail = MutableStateFlow<ProductDetailResponseDto?>(null)
    val productDetail: StateFlow<ProductDetailResponseDto?> = _productDetail

    private val _chatDetail = MutableStateFlow<ChatDetailResponseDto?>(null)
    val chatDetail: StateFlow<ChatDetailResponseDto?> = _chatDetail

    private val _initialMessages = MutableStateFlow<List<ChatMessageDto>>(emptyList())
    val initialMessages: StateFlow<List<ChatMessageDto>> = _initialMessages

    fun getChatList(onError: (Throwable) -> Unit = {}) {
        viewModelScope.launch {
            chatRepository.getChatList()
                .onSuccess {
                    _chatList.value = it.chatRooms
                }
                .onFailure(onError)
        }
    }

    fun getProductDetail(productId: Int, onError: (Throwable) -> Unit = {}) {
        viewModelScope.launch {
            productRepository.getProductDetail(productId)
                .onSuccess {
                    _productDetail.value = it
                }
                .onFailure(onError)
        }
    }

    fun getChatDetail(chatRoomMessageId: String, onError: (Throwable) -> Unit = {}) {
        viewModelScope.launch {
            val skip = 0
            val listSize = 30
            chatRepository.getChatDetail(chatRoomMessageId, skip, listSize)
                .onSuccess {
                    _chatDetail.value = it
                    _initialMessages.value += it.messages
                }
                .onFailure(onError)
        }
    }

}