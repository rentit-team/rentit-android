package com.example.rentit.feature.chat

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.storage.getMyIdFromPrefs
import com.example.rentit.common.storage.getToken
import com.example.rentit.common.websocket.WebSocketManager
import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.chat.dto.ChatRoomSummaryDto
import com.example.rentit.data.chat.repository.ChatRepository
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.repository.ProductRepository
import com.example.rentit.feature.chat.model.ChatMessageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val chatRepository: ChatRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val myId: Long = getMyIdFromPrefs(context)

    private val _chatList = MutableStateFlow<List<ChatRoomSummaryDto>>(emptyList())
    val chatList: StateFlow<List<ChatRoomSummaryDto>> = _chatList

    private val _productDetail = MutableStateFlow<ProductDetailResponseDto?>(null)
    val productDetail: StateFlow<ProductDetailResponseDto?> = _productDetail

    private val _chatDetail = MutableStateFlow<ChatDetailResponseDto?>(null)
    val chatDetail: StateFlow<ChatDetailResponseDto?> = _chatDetail

    private val _initialMessages = MutableStateFlow<List<ChatMessageUiModel>>(emptyList())
    val initialMessages: StateFlow<List<ChatMessageUiModel>> = _initialMessages

    private val _realTimeMessages = MutableStateFlow<List<ChatMessageUiModel>>(emptyList())
    val realTimeMessages: StateFlow<List<ChatMessageUiModel>> = _realTimeMessages

    private val _senderNickname = MutableStateFlow<String?>(null)
    val senderNickname: StateFlow<String?> = _senderNickname

    @RequiresApi(Build.VERSION_CODES.O)
    fun connectWebSocket(chatroomId: String, onConnect: () -> Unit) {
        val token = getToken(context) ?: return
        WebSocketManager.connect(chatroomId, myId, token, onConnect) {
            _realTimeMessages.value = listOf(it) + _realTimeMessages.value
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(chatroomId: String, message: String) {
        WebSocketManager.sendMessage(chatroomId, myId, message)
    }

    fun disconnectWebSocket() {
        WebSocketManager.disconnect()
    }

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
                .onSuccess { detail ->
                    _chatDetail.value = detail
                    _initialMessages.value =
                        detail.messages.map { ChatMessageUiModel(it.isMine, it.content, it.sentAt) }
                    _senderNickname.value =
                        detail.chatRoom.participants.find { it.userId != myId }?.nickname
                }
                .onFailure(onError)
        }
    }

}