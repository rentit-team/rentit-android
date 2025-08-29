package com.example.rentit.presentation.chat.chatroom

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.AutoMsgType
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.websocket.WebSocketManager
import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.rental.dto.UpdateRentalStatusRequestDto
import com.example.rentit.domain.chat.repository.ChatRepository
import com.example.rentit.domain.product.repository.ProductRepository
import com.example.rentit.domain.rental.repository.RentalRepository
import com.example.rentit.domain.user.repository.UserRepository
import com.example.rentit.presentation.chat.chatroom.model.ChatMessageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository,
    private val productRepository: ProductRepository,
    private val rentalRepository: RentalRepository
): ViewModel() {

    private val authUserId: Long = userRepository.getAuthUserIdFromPrefs()

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
                        detail.chatRoom.participants.find { it.userId != authUserId }?.nickname
                }
                .onFailure(onError)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun connectWebSocket(chatRoomId: String, onConnect: () -> Unit) {
        val token = userRepository.getTokenFromPrefs() ?: return
        WebSocketManager.connect(chatRoomId, token, onConnect) { data ->
            val msg = ChatMessageUiModel(data.senderId == authUserId, data.content, data.sentAt)
            _realTimeMessages.value = listOf(msg) + _realTimeMessages.value
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMessage(chatRoomId: String, message: String) {
        WebSocketManager.sendMessage(chatRoomId, authUserId, message)
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
    fun acceptRentalRequest(chatroomId: String, productId: Int, reservationId: Int, onSuccess: () -> Unit = {}, onError: (Throwable) -> Unit = {}) {
        viewModelScope.launch {
            rentalRepository.updateRentalStatus(
                productId,
                reservationId,
                UpdateRentalStatusRequestDto(RentalStatus.ACCEPTED)
            ).onSuccess {
                onSuccess()
                sendMessage(chatroomId, AutoMsgType.REQUEST_ACCEPT.code)
            }.onFailure(onError)
        }
    }
}