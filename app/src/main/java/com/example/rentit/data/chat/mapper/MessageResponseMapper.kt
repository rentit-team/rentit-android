package com.example.rentit.data.chat.mapper

import com.example.rentit.data.chat.dto.MessageResponseDto
import com.example.rentit.domain.chat.model.ChatMessageModel

fun MessageResponseDto.toChatMessageModel(isMine: Boolean) =
    ChatMessageModel(
        messageId = messageId,
        isMine = isMine,
        message = content,
        sentAt = sentAt
    )