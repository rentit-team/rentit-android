package com.example.rentit.data.rental.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.domain.rental.model.RentalPeriodModel
import com.example.rentit.common.util.parseLocalDateOrNull
import com.example.rentit.common.util.parseLocalDateTimeOrNull
import com.example.rentit.data.rental.dto.RentalHistoryDto
import com.example.rentit.domain.rental.model.RentalHistoryModel

@RequiresApi(Build.VERSION_CODES.O)
fun RentalHistoryDto.toModel() =
    RentalHistoryModel(
        reservationId = reservationId,
        status = status,
        renterNickName = renterNickName,
        rentalPeriod = RentalPeriodModel(
            startDate = parseLocalDateOrNull(startDate),
            endDate = parseLocalDateOrNull(endDate),
        ),
        requestedAt = parseLocalDateTimeOrNull(requestedAt)
    )