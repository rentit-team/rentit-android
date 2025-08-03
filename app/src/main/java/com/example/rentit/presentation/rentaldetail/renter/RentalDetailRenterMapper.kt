package com.example.rentit.presentation.rentaldetail.renter

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.presentation.rentaldetail.renter.model.RentalStatusRenterUiModel

@RequiresApi(Build.VERSION_CODES.O)
fun RentalDetailResponseDto.toUiModel(): RentalStatusRenterUiModel {

    return when (rental.status) {

        in RentalStatus.PENDING.name,
        RentalStatus.ACCEPTED.name,
        RentalStatus.REJECTED.name,
        RentalStatus.CANCELED.name -> RentalStatusRenterUiModel.Request(
            status = rental.status,
            productTitle = rental.product.title,
            startDate = rental.startDate,
            endDate = rental.endDate,
            totalPrice = rental.totalAmount,
            deposit = rental.depositAmount
        )

        RentalStatus.PAID.name -> RentalStatusRenterUiModel.Paid

        RentalStatus.RENTING.name -> RentalStatusRenterUiModel.Renting

        RentalStatus.RETURNED.name -> RentalStatusRenterUiModel.Returned

        else -> RentalStatusRenterUiModel.Unknown
    }
}
