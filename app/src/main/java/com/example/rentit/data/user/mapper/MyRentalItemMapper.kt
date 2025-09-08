package com.example.rentit.data.user.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.user.dto.ReservationDto
import com.example.rentit.domain.user.model.MyRentalItemModel

@RequiresApi(Build.VERSION_CODES.O)
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