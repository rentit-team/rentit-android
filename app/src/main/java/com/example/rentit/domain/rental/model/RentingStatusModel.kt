package com.example.rentit.domain.rental.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.rentit.R
import com.example.rentit.common.theme.AppGreen
import com.example.rentit.common.theme.AppRed
import com.example.rentit.common.theme.SecondaryYellow

enum class RentingStatus(
    @StringRes val strRes: Int,
    val textColor: Color,
    @StringRes val subLabelStrRes: Int?,
) {
    RENTING_IN_USE(
        strRes = R.string.rental_status_renting,
        textColor = SecondaryYellow,
        subLabelStrRes = R.string.screen_rental_detail_renter_renting_day_before_return
    ),
    RENTING_RETURN_DAY(
        strRes = R.string.rental_status_renting_return_day,
        textColor = AppGreen,
        subLabelStrRes = null
    ),
    RENTING_OVERDUE(
        strRes = R.string.rental_status_renting_return_overdue,
        textColor = AppRed,
        subLabelStrRes = R.string.screen_rental_detail_renter_renting_day_overdue
    );

    companion object {
        fun fromDaysFromReturnDate(daysFromReturnDate: Int): RentingStatus = when {
            daysFromReturnDate >= 1 -> RENTING_IN_USE
            daysFromReturnDate == 0 -> RENTING_RETURN_DAY
            else -> RENTING_OVERDUE
        }
    }
}