package com.example.rentit.domain.chat.usecase

import com.example.rentit.data.chat.dto.MessageResponseDto
import com.example.rentit.data.chat.mapper.toChatMessageModel
import com.example.rentit.domain.chat.model.ChatMessageModel
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

/**
 * 서버에서 받은 MessageResponseDto를 도메인 모델(ChatMessageModel)로 변환하는 UseCase
 *
 * - 현재 로그인한 사용자의 ID를 기준으로 메시지가 본인 메시지인지 구분
 * - Presentation Layer 또는 다른 도메인 로직에서 바로 사용할 수 있는 모델 반환
 */

class ConvertMessageUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(messageResponse: MessageResponseDto): ChatMessageModel {
        val authUserId = userRepository.getAuthUserIdFromPrefs()

        return messageResponse.toChatMessageModel(authUserId == messageResponse.senderId)
    }
}