package com.example.rentit.data.chat.mapper

import com.example.rentit.data.chat.dto.MessageResponseDto
import com.example.rentit.domain.chat.model.ChatMessageModel

fun MessageResponseDto.toChatMessageModel(authUserId: Long) =
    ChatMessageModel(
        messageId = messageId,
        isMine = authUserId == senderId,
        message = content,
        sentAt = sentAt
    )