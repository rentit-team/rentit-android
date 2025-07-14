package com.example.rentit.presentation.chat.chatroom

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.AutoMsgType
import com.example.rentit.common.enums.ResvStatus
import com.example.rentit.common.storage.getMyIdFromPrefs
import com.example.rentit.common.storage.getToken
import com.example.rentit.common.websocket.WebSocketManager
import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.chat.repository.ChatRepository
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.dto.UpdateResvStatusRequestDto
import com.example.rentit.data.product.repository.ProductRepository
import com.example.rentit.presentation.chat.chatroom.model.ChatMessageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val chatRepository: ChatRepository,
    private val productRepository: ProductRepository
): ViewModel() {

    private val myId: Long = getMyIdFromPrefs(context)

    private val _productDetail = MutableStateFlow<ProductDetailResponseDto?>(null)
    val productDetail: StateFlow<ProductDetailResponseDto?> = _productDetail

    private val _chatDetail = MutableStateFlow<ChatDetailResponseDto?>(null)
    val chatDetail: StateFlow<ChatDetailResponseDto?> = _chatDetail

    // 백엔드 오류로 인한 임시 요청 확인 처리
    private val _isRequestAccepted = MutableStateFlow(false)
    val isRequestAccepted: StateFlow<Boolean> = _isRequestAccepted

    private val _initialMessages = MutableStateFlow<List<ChatMessageUiModel>>(emptyList())
    val initialMessages: StateFlow<List<ChatMessageUiModel>> = _initialMessages

    private val _realTimeMessages = MutableStateFlow<List<ChatMessageUiModel>>(emptyList())
    val realTimeMessages: StateFlow<List<ChatMessageUiModel>> = _realTimeMessages

    private val _senderNickname = MutableStateFlow<String?>(null)
    val senderNickname: StateFlow<String?> = _senderNickname

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun connectWebSocket(chatRoomId: String, onConnect: () -> Unit) {
        val token = getToken(context) ?: return
        WebSocketManager.connect(chatRoomId, token, onConnect) { data ->
            val msg = ChatMessageUiModel(data.senderId == myId, data.content, data.sentAt)
            _realTimeMessages.value = listOf(msg) + _realTimeMessages.value
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(chatRoomId: String, message: String) {
        WebSocketManager.sendMessage(chatRoomId, myId, message)
    }

    fun disconnectWebSocket() {
        WebSocketManager.disconnect()
    }

    fun resetRealTimeMessages() {
        _realTimeMessages.value = emptyList()
    }

    // 백엔드 오류로 인한 임시 요청 확인 처리
    fun checkRequestAccepted() {
        if(_initialMessages.value.find { it.message == AutoMsgType.REQUEST_ACCEPT.code } != null) {
            _isRequestAccepted.value = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateResvStatus(chatroomId: String, productId: Int, reservationId: Int, onSuccess: () -> Unit = {}, onError: (Throwable) -> Unit = {}) {
        viewModelScope.launch {
            productRepository.updateResvStatus(
                productId,
                reservationId,
                UpdateResvStatusRequestDto(ResvStatus.ACCEPTED)
            )
                .onSuccess {
                    onSuccess()
                    sendMessage(chatroomId, AutoMsgType.REQUEST_ACCEPT.code)
                }
                .onFailure(onError)
        }
    }
}