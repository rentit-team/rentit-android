package com.example.rentit.domain.chat.model

import com.example.rentit.common.enums.RentalStatus
import java.time.LocalDate

data class ChatRoomRentalSummaryModel (
    val reservationId: Int,
    val renterId: Long,
    val status: RentalStatus,
    val startDate: LocalDate,
    val endDate: LocalDate,
)