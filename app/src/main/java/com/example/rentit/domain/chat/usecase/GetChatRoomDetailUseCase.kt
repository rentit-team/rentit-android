package com.example.rentit.domain.chat.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.domain.chat.exception.NotChatRoomMemberException
import com.example.rentit.domain.chat.model.ChatMessageModel
import com.example.rentit.domain.chat.model.ChatRoomDetailModel
import com.example.rentit.domain.chat.repository.ChatRepository
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class GetChatRoomDetailUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatRepository
) {
    companion object {
        private const val DEFAULT_PAGE = 0
        private const val DEFAULT_PAGE_SIZE = 10
    }

    suspend operator fun invoke(chatRoomId: String): Result<ChatRoomDetailModel> {
        return runCatching {
            val authUserId = userRepository.getAuthUserIdFromPrefs()
            val chatRoomDetail = chatRepository.getChatDetail(chatRoomId, DEFAULT_PAGE, DEFAULT_PAGE_SIZE).getOrThrow()
            val participants = chatRoomDetail.chatRoom.participants

            if(participants.find { it.userId == authUserId } == null) throw NotChatRoomMemberException()

            val partner = participants.find { it.userId != authUserId }

            ChatRoomDetailModel(
                // TODO: 수정 필요 - 백엔드에서 productId와 reservationId 순서가 교차되어 있음
                reservationId = chatRoomDetail.chatRoom.productId,
                productId = chatRoomDetail.chatRoom.reservationId,
                partnerNickname = partner?.nickname ?: "",
                messages = chatRoomDetail.messages.map {
                    ChatMessageModel(
                        messageId = it.messageId,
                        isMine = it.sender.userId == authUserId,
                        message = it.content,
                        sentAt = it.sentAt
                    )
                }
            )
        }
    }
}