package com.example.rentit.domain.chat.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.core.error.ForbiddenException
import com.example.rentit.domain.chat.model.ChatMessageModel
import com.example.rentit.domain.chat.model.ChatRoomDetailModel
import com.example.rentit.domain.chat.repository.ChatRepository
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

/**
 * 채팅방 상세 정보를 가져오는 UseCase
 *
 * - 현재 로그인한 사용자가 채팅방 참가자인지 확인
 * - 채팅 참가자가 아닌 경우 ForbiddenException 발생
 * - 채팅방의 상대방 정보와 메시지 리스트를 도메인 모델로 변환
 */

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

            if(participants.find { it.userId == authUserId } == null) throw ForbiddenException()

            val partner = participants.find { it.userId != authUserId }

            ChatRoomDetailModel(
                reservationId = chatRoomDetail.chatRoom.reservationId,
                productId = chatRoomDetail.chatRoom.productId,
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