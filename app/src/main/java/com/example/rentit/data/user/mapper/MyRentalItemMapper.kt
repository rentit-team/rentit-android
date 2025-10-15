package com.example.rentit.data.user.mapper

import com.example.rentit.data.user.dto.ReservationDto
import com.example.rentit.domain.user.model.MyRentalItemModel

fun ReservationDto.toMyRentalItemModel() =
    MyRentalItemModel(
        productId = product.productId,
        reservationId = reservationId,
        productTitle = product.title,
        thumbnailImgUrl = product.thumbnailImgUrl,
        startDate = startDate,
        endDate = endDate,
        status = status,
        requestedAt = requestedAt
    )