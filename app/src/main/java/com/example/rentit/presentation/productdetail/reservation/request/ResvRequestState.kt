package com.example.rentit.presentation.productdetail.reservation.request

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.common.util.inclusiveDaysBetween
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class ResvRequestState(
    val rentalStartDate: LocalDate? = null,
    val rentalEndDate: LocalDate? = null,
    val minPeriod: Int? = 0,
    val maxPeriod: Int? = 0,
    val rentalPrice: Int = 0,
    val deposit: Int = 0,
    val totalRentalPrice: Int = 0,
    val reservedDateList: List<String> = emptyList(),
) {
    val rentalPeriod: Int
        get() = inclusiveDaysBetween(rentalStartDate, rentalEndDate)

    val totalPrice: Int
        get() = rentalPrice * rentalPeriod + deposit
}