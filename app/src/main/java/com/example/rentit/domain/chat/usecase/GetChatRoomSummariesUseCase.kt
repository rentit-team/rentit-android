package com.example.rentit.domain.chat.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.domain.chat.model.ChatRoomSummaryModel
import com.example.rentit.domain.chat.repository.ChatRepository
import java.time.OffsetDateTime
import javax.inject.Inject

/**
 * 채팅방 목록 요약 정보를 가져오는 UseCase
 *
 * - 서버에서 채팅방 리스트를 가져옴
 * - 각 채팅방의 마지막 메시지 시간과 기본 정보를 도메인 모델(ChatRoomSummaryModel)로 변환
 * - Presentation Layer에서 바로 사용 가능
 */

@RequiresApi(Build.VERSION_CODES.O)
class GetChatRoomSummariesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): Result<List<ChatRoomSummaryModel>> {
        return runCatching {
            val chatList = chatRepository.getChatList().getOrThrow().chatRooms

            chatList.map {
                val parsedLastMessageTime = it.lastMessageTime?.let { time -> OffsetDateTime.parse(time) }
                ChatRoomSummaryModel(
                    chatRoomId = it.chatRoomId,
                    productTitle = it.productTitle,
                    thumbnailImgUrl = it.thumbnailImgUrl,
                    partnerNickname = it.partnerNickname,
                    lastMessage = it.lastMessage,
                    lastMessageTime = parsedLastMessageTime
                )
            }.sortedByDescending { it.lastMessageTime }
        }
    }
}