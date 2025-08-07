package com.example.rentit.presentation.rentaldetail.renter

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.util.daysFromToday
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.common.model.RentalSummaryUiModel
import com.example.rentit.presentation.rentaldetail.model.RentingStatus
import com.example.rentit.presentation.rentaldetail.renter.stateui.RenterRentalStatusUiModel

private const val TAG = "RentalStatus"

@RequiresApi(Build.VERSION_CODES.O)
fun RentalDetailResponseDto.toRenterUiModel(): RenterRentalStatusUiModel {
    val status = runCatching { RentalStatus.valueOf(rental.status) }
        .onFailure { Log.w(TAG, "Unknown rental status: ${rental.status}")  }
        .getOrNull()

    val rentalSummary = RentalSummaryUiModel(
        productTitle = rental.product.title,
        thumbnailImgUrl = rental.product.thumbnailImgUrl,
        startDate = rental.startDate,
        endDate = rental.endDate,
        totalPrice = rental.totalAmount,
    )

    val basicRentalFee = rental.totalAmount - rental.depositAmount

    return when (status) {
        RentalStatus.PENDING,
        RentalStatus.ACCEPTED,
        RentalStatus.REJECTED,
        RentalStatus.CANCELED -> {
            RenterRentalStatusUiModel.Request(
                status = status,
                isAccepted = status == RentalStatus.ACCEPTED,
                rentalSummary = rentalSummary,
                basicRentalFee = basicRentalFee,
                deposit = rental.depositAmount
            )
        }

        RentalStatus.PAID -> {
            val daysUntilRental = daysFromToday(rental.startDate)
            RenterRentalStatusUiModel.Paid(
                status = status,
                daysUntilRental = daysUntilRental,
                rentalSummary = rentalSummary,
                deposit = rental.depositAmount,
                basicRentalFee = basicRentalFee,
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
            val isReturnAvailable = daysFromReturnDate <= -1
            RenterRentalStatusUiModel.Renting(
                status = rentingStatus,
                isOverdue = rentingStatus == RentingStatus.RENTING_OVERDUE,
                isReturnAvailable = isReturnAvailable,
                daysFromReturnDate = daysFromReturnDate,
                rentalSummary = rentalSummary,
                deposit = rental.depositAmount,
                basicRentalFee = basicRentalFee,
                isReturnPhotoRegistered = rental.returnStatus.isPhotoRegistered,
                isReturnTrackingNumRegistered = rental.returnStatus.isTrackingNumberRegistered,
                rentalTrackingNumber = rental.rentalTrackingNumber
            )
        }

        RentalStatus.RETURNED -> RenterRentalStatusUiModel.Returned(
            status = status,
            rentalSummary = rentalSummary,
            deposit = rental.depositAmount,
            basicRentalFee = basicRentalFee,
            rentalTrackingNumber = rental.rentalTrackingNumber,
            returnTrackingNumber = rental.returnTrackingNumber
        )

        null -> RenterRentalStatusUiModel.Unknown
    }
}
