package com.example.rentit.domain.chat.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.domain.chat.model.ChatRoomSummaryModel
import com.example.rentit.domain.chat.repository.ChatRepository
import java.time.OffsetDateTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class GetChatRoomSummariesUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {
    suspend operator fun invoke(): Result<List<ChatRoomSummaryModel>> {
        return runCatching {
            val chatList = chatRepository.getChatList().getOrThrow().chatRooms

            chatList.map {
                val localDateTime = it.lastMessageTime?.let { time -> OffsetDateTime.parse(time) }
                ChatRoomSummaryModel(
                    chatRoomId = it.chatRoomId,
                    productTitle = it.productTitle,
                    thumbnailImgUrl = it.thumbnailImgUrl,
                    partnerNickname = it.partnerNickname,
                    lastMessage = it.lastMessage,
                    lastMessageTime = localDateTime
                )
            }
        }
    }
}