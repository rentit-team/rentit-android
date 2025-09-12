package com.example.rentit.common.uimodel

import java.time.LocalDate

data class RentalPeriodModel(
    val startDate: LocalDate?,
    val endDate: LocalDate?
)