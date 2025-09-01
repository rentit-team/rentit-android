package com.example.rentit.data.rental.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.util.daysFromToday
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.common.uimodel.RentalSummaryUiModel
import com.example.rentit.presentation.rentaldetail.model.RentingStatus
import com.example.rentit.presentation.rentaldetail.owner.stateui.OwnerRentalStatusUiModel

@RequiresApi(Build.VERSION_CODES.O)
fun RentalDetailResponseDto.toOwnerUiModel(): OwnerRentalStatusUiModel {
    val status = rental.status

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
            OwnerRentalStatusUiModel.Request(
                status = status,
                isPending = status == RentalStatus.PENDING,
                isAccepted = status == RentalStatus.ACCEPTED,
                rentalSummary = rentalSummary,
                basicRentalFee = basicRentalFee,
            )
        }

        RentalStatus.PAID -> {
            val daysUntilRental = daysFromToday(rental.startDate)
            OwnerRentalStatusUiModel.Paid(
                status = status,
                daysUntilRental = daysUntilRental,
                rentalSummary = rentalSummary,
                basicRentalFee = basicRentalFee,
                isSendingPhotoRegistered = rental.deliveryStatus.isPhotoRegistered,
                isSendingTrackingNumRegistered = rental.deliveryStatus.isTrackingNumberRegistered,
                rentalTrackingNumber = rental.rentalTrackingNumber,
            )
        }

        RentalStatus.RENTING -> {
            val daysFromReturnDate = daysFromToday(rental.endDate)
            val rentingStatus = RentingStatus.fromDaysFromReturnDate(daysFromReturnDate)

            OwnerRentalStatusUiModel.Renting(
                status = rentingStatus,
                isOverdue = rentingStatus == RentingStatus.RENTING_OVERDUE,
                daysFromReturnDate = daysFromReturnDate,
                rentalSummary = rentalSummary,
                deposit = rental.depositAmount,
                basicRentalFee = basicRentalFee,
                rentalTrackingNumber = rental.rentalTrackingNumber
            )
        }

        RentalStatus.RETURNED -> OwnerRentalStatusUiModel.Returned(
            status = status,
            rentalSummary = rentalSummary,
            deposit = rental.depositAmount,
            basicRentalFee = basicRentalFee,
            rentalTrackingNumber = rental.rentalTrackingNumber,
            returnTrackingNumber = rental.returnTrackingNumber
        )
    }
}
