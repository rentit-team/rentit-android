package com.example.rentit.domain.rental.model

import com.example.rentit.common.enums.RentalStatus
import java.time.LocalDate

data class RentalChatRoomSummaryModel (
    val reservationId: Int,
    val renterId: Long,
    val status: RentalStatus,
    val startDate: LocalDate,
    val endDate: LocalDate,
)