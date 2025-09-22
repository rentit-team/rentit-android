package com.example.rentit.data.chat.mapper

import com.example.rentit.data.chat.dto.ChatMessageDto
import com.example.rentit.domain.chat.model.ChatMessageModel

fun ChatMessageDto.toModel(isMine: Boolean) =
    ChatMessageModel(
        messageId = messageId,
        isMine = isMine,
        message = content,
        sentAt = sentAt
    )