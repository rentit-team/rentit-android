package com.example.rentit.domain.chat.model

import com.example.rentit.common.enums.RentalStatus
import java.time.LocalDate

data class ChatRentalSummaryModel (
    val reservationId: Int,
    val status: RentalStatus,
    val startDate: LocalDate,
    val endDate: LocalDate,
)