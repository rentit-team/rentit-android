package com.example.rentit.presentation.rentaldetail.renter.model

import androidx.compose.ui.graphics.Color
import com.example.rentit.R
import com.example.rentit.common.theme.AppGreen
import com.example.rentit.common.theme.AppRed
import com.example.rentit.common.theme.SecondaryYellow

enum class RentingStatus(val strRes: Int, val textColor: Color) {
    RENTING_IN_USE(R.string.rental_status_renting, SecondaryYellow),
    RENTING_RETURN_DAY(R.string.rental_status_renting_return_day, AppGreen),
    RENTING_OVERDUE(R.string.rental_status_renting_return_overdue, AppRed)
}