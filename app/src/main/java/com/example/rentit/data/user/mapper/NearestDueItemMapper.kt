package com.example.rentit.data.user.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.user.dto.NearestDueItemDto
import com.example.rentit.domain.user.model.NearestDueItemModel

@RequiresApi(Build.VERSION_CODES.O)
fun NearestDueItemDto.toModel() =
    NearestDueItemModel(
        reservationId = reservationId,
        productId = 0,  // TODO: API 수정 후 productId 값 변경
        productTitle = productTitle,
        remainingRentalDays = daysUntilReturn
    )