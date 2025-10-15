package com.example.rentit.data.user.mapper

import com.example.rentit.data.user.dto.NearestDueItemDto
import com.example.rentit.domain.user.model.NearestDueItemModel

fun NearestDueItemDto.toModel() =
    NearestDueItemModel(
        reservationId = reservationId,
        productId = productId,
        productTitle = productTitle,
        remainingRentalDays = daysUntilReturn
    )