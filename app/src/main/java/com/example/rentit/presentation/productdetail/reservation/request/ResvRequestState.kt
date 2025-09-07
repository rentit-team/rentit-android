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
    private val rentalPrice: Int = 0,
    val deposit: Int = 0,
    val reservedDateList: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val showNetworkErrorDialog: Boolean = false,
    val showServerErrorDialog: Boolean = false,
    val showAccessNotAllowedDialog: Boolean = false,
    val showResvAlreadyExistDialog: Boolean = false
) {
    val selectedPeriod: Int
        get() = inclusiveDaysBetween(rentalStartDate, rentalEndDate)

    val totalRentalPrice: Int
        get() = rentalPrice * selectedPeriod

    val totalPrice: Int
        get() = totalRentalPrice + deposit
}