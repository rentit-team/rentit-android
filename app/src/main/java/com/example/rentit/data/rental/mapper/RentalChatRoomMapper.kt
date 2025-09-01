package com.example.rentit.data.rental.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.rental.dto.RentalDto
import com.example.rentit.domain.rental.model.RentalChatRoomSummaryModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun RentalDto.toChatRoomSummaryModel() =
    RentalChatRoomSummaryModel(
        reservationId = reservationId,
        status = status,
        startDate = LocalDate.parse(startDate),
        endDate = LocalDate.parse(endDate)
    )