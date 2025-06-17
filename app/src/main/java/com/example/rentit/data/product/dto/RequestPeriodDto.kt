package com.example.rentit.data.product.dto

import java.time.LocalDate

data class RequestPeriodDto(
    val startDate: LocalDate,
    val endDate: LocalDate,
)