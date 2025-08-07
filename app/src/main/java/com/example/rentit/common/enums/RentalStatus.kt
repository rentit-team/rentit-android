package com.example.rentit.common.enums

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.rentit.R
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.SecondaryYellow

enum class RentalStatus(@StringRes val strRes: Int, val textColor: Color) {
    PENDING(R.string.rental_status_pending, AppBlack),
    ACCEPTED(R.string.rental_status_accepted, PrimaryBlue500),
    REJECTED(R.string.rental_status_rejected, Gray400),
    PAID(R.string.rental_status_paid, PrimaryBlue500),
    RENTING(R.string.rental_status_renting, SecondaryYellow),
    RETURNED(R.string.rental_status_returned, PrimaryBlue500),
    CANCELED(R.string.rental_status_canceled, Gray400)
}