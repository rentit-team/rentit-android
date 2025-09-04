package com.example.rentit.domain.chat.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.chat.dto.MessageResponseDto
import com.example.rentit.data.chat.mapper.toChatMessageModel
import com.example.rentit.domain.chat.model.ChatMessageModel
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class ConvertMessageUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    fun execute(messageResponse: MessageResponseDto): ChatMessageModel {
        val authUserId = userRepository.getAuthUserIdFromPrefs()

        return messageResponse.toChatMessageModel(authUserId)
    }
}