package com.example.rentit.domain.rental.model

import java.time.LocalDate

data class RentalPeriodModel(
    val startDate: LocalDate?,
    val endDate: LocalDate?
)