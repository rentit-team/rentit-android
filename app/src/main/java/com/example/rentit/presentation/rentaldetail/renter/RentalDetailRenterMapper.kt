package com.example.rentit.presentation.rentaldetail.renter

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.common.DEPOSIT_BASIS_DAYS
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.util.daysBetween
import com.example.rentit.data.product.dto.RentalDetailDto
import com.example.rentit.presentation.rentaldetail.renter.model.RentalStatusRenterUiModel

@RequiresApi(Build.VERSION_CODES.O)
fun RentalDetailDto.toUiModel(): RentalStatusRenterUiModel {
    val period = daysBetween(startDate, endDate)

    return when (rentalStatus) {

        in RentalStatus.PENDING.name,
        RentalStatus.ACCEPTED.name,
        RentalStatus.REJECTED.name,
        RentalStatus.CANCELED.name -> RentalStatusRenterUiModel.Request(
            status = rentalStatus,
            productTitle = productTitle,
            startDate = startDate,
            endDate = endDate,
            pricePerDay = pricePerDay,
            basicRentalPrice = pricePerDay * period,
            deposit = pricePerDay * DEPOSIT_BASIS_DAYS
        )

        RentalStatus.PAID.name -> RentalStatusRenterUiModel.Paid

        RentalStatus.RENTING.name -> RentalStatusRenterUiModel.RENTING

        RentalStatus.RETURNED.name -> RentalStatusRenterUiModel.Returned

        else -> RentalStatusRenterUiModel.Unknown
    }
}
