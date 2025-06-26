package com.example.rentit.feature.chat

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.R
import com.example.rentit.data.chat.dto.ChangedByDto
import com.example.rentit.data.chat.dto.ChatDetailResponseDto
import com.example.rentit.data.chat.dto.ChatMessageDto
import com.example.rentit.data.chat.dto.ChatParticipantDto
import com.example.rentit.data.chat.dto.ChatRoomDetailDto
import com.example.rentit.data.chat.dto.ChatRoomSummaryDto
import com.example.rentit.data.chat.dto.SenderDto
import com.example.rentit.data.chat.dto.StatusHistoryDto
import com.example.rentit.data.chat.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {
    val exampleChatDetailResponse = ChatDetailResponseDto(
        chatRoom = ChatRoomDetailDto(
            chatroomId = "room_1234567890",
            participants = listOf(
                ChatParticipantDto(
                    userId = "user_abc",
                    nickname = "김숙명",
                    role = "buyer",
                    profileImageUrl = "https://cdn.example.com/user_abc.png"
                ),
                ChatParticipantDto(
                    userId = "user_xyz",
                    nickname = "홍길동",
                    role = "seller",
                    profileImageUrl = "https://cdn.example.com/user_xyz.png"
                )
            ),
            statusHistory = listOf(
                StatusHistoryDto(
                    status = "PENDING",
                    changedAt = "2025-03-25T09:00:00Z",
                    changedBy = ChangedByDto(
                        userId = 1,
                        nickname = "김숙명"
                    )
                ),
                StatusHistoryDto(
                    status = "ACCEPTED",
                    changedAt = "2025-03-25T10:00:00Z",
                    changedBy = ChangedByDto(
                        userId = 2,
                        nickname = "홍길동"
                    )
                ),
                StatusHistoryDto(
                    status = "PENDING",
                    changedAt = "2025-03-25T10:00:00Z",
                    changedBy = ChangedByDto(
                        userId = 1,
                        nickname = "김숙명"
                    )
                )
            )
        ),
        messages = listOf(
            ChatMessageDto(
                messageId = "msg_001",
                sender = SenderDto(
                    userId = 2,
                    nickname = "홍길동"
                ),
                content = "요청보고 연락드렸습니다.",
                sentAt = "2025-03-25T09:30:00Z",
                type = "TEXT",
                isMine = false
            ),
            ChatMessageDto(
                messageId = "msg_002",
                sender = SenderDto(
                    userId = 1,
                    nickname = "김숙명"
                ),
                content = "가나다라",
                sentAt = "2025-03-25T09:31:00Z",
                type = "TEXT",
                isMine = true
            )
        ),
        hasNext = false,
        totalPage = 2
    )

    private val _chatList = MutableStateFlow<List<ChatRoomSummaryDto>>(emptyList())
    val chatList: StateFlow<List<ChatRoomSummaryDto>> = _chatList

    fun getChatList(onError: (Throwable) -> Unit = {}) {
        viewModelScope.launch {
            chatRepository.getChatList()
                .onSuccess {
                    _chatList.value = it.chatRooms
                }
                .onFailure(onError)
        }
    }

}