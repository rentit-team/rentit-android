package com.example.rentit.data.rental.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.rental.dto.RentalDto
import com.example.rentit.domain.chat.model.ChatRoomRentalSummaryModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
fun RentalDto.toChatRoomSummaryModel() =
    ChatRoomRentalSummaryModel(
        reservationId = reservationId,
        renterId = renter.userId,
        status = status,
        startDate = LocalDate.parse(startDate),
        endDate = LocalDate.parse(endDate)
    )