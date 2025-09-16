package com.example.rentit.data.user.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.user.dto.NearestDueItemDto
import com.example.rentit.domain.user.model.NearestDueItemModel

@RequiresApi(Build.VERSION_CODES.O)
fun NearestDueItemDto.toModel() =
    NearestDueItemModel(
        reservationId = reservationId,
        productId = productId,
        productTitle = productTitle,
        remainingRentalDays = daysUntilReturn
    )