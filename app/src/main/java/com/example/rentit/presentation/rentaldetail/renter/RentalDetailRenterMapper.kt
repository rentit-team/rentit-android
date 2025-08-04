package com.example.rentit.presentation.rentaldetail.renter

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.util.daysFromToday
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.presentation.rentaldetail.renter.model.RentalStatusRenterUiModel
import com.example.rentit.presentation.rentaldetail.renter.model.RentingStatus

@RequiresApi(Build.VERSION_CODES.O)
fun RentalDetailResponseDto.toUiModel(): RentalStatusRenterUiModel {
    val status = runCatching { RentalStatus.valueOf(rental.status) }.getOrNull()

    return when (status) {
        RentalStatus.PENDING,
        RentalStatus.ACCEPTED,
        RentalStatus.REJECTED,
        RentalStatus.CANCELED -> {
            RentalStatusRenterUiModel.Request(
                status = status,
                isAccepted = status == RentalStatus.ACCEPTED,
                productTitle = rental.product.title,
                thumbnailImgUrl = rental.product.thumbnailImgUrl,
                startDate = rental.startDate,
                endDate = rental.endDate,
                totalPrice = rental.totalAmount,
                deposit = rental.depositAmount
            )
        }

        RentalStatus.PAID -> {
            val daysUntilRental = daysFromToday(rental.startDate)
            RentalStatusRenterUiModel.Paid(
                status = status,
                daysUntilRental = daysUntilRental,
                productTitle = rental.product.title,
                thumbnailImgUrl = rental.product.thumbnailImgUrl,
                startDate = rental.startDate,
                endDate = rental.endDate,
                totalPrice = rental.totalAmount,
                deposit = rental.depositAmount,
                rentalTrackingNumber = rental.rentalTrackingNumber
            )
        }

        RentalStatus.RENTING -> {
            val daysFromReturnDate = daysFromToday(rental.endDate)
            val rentingStatus = when {
                daysFromReturnDate >= 0 -> RentingStatus.RENTING_IN_USE
                daysFromReturnDate == -1 -> RentingStatus.RENTING_RETURN_DAY
                else -> RentingStatus.RENTING_OVERDUE
            }
            val isReturnAvailable = daysFromReturnDate >= -1
            RentalStatusRenterUiModel.Renting(
                status = rentingStatus,
                isOverdue = rentingStatus == RentingStatus.RENTING_OVERDUE,
                isReturnAvailable = isReturnAvailable,
                daysFromReturnDate = daysFromReturnDate,
                productTitle = rental.product.title,
                thumbnailImgUrl = rental.product.thumbnailImgUrl,
                startDate = rental.startDate,
                endDate = rental.endDate,
                totalPrice = rental.totalAmount,
                deposit = rental.depositAmount,
                isReturnPhotoRegistered = rental.returnStatus.isPhotoRegistered,
                isReturnTrackingNumRegistered = rental.returnStatus.isTrackingNumberRegistered,
                rentalTrackingNumber = rental.rentalTrackingNumber
            )
        }

        RentalStatus.RETURNED -> RentalStatusRenterUiModel.Returned(
            status = status,
            productTitle = rental.product.title,
            thumbnailImgUrl = rental.product.thumbnailImgUrl,
            startDate = rental.startDate,
            endDate = rental.endDate,
            totalPrice = rental.totalAmount,
            deposit = rental.depositAmount,
            rentalTrackingNumber = rental.rentalTrackingNumber
        )

        null -> RentalStatusRenterUiModel.Unknown
    }
}
