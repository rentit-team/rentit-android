package com.example.rentit.data.rental.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.util.daysFromToday
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.common.uimodel.RentalSummaryUiModel
import com.example.rentit.domain.rental.model.RentalDetailStatusModel
import com.example.rentit.common.enums.RentingStatus

@RequiresApi(Build.VERSION_CODES.O)
fun RentalDetailResponseDto.toModel(): RentalDetailStatusModel {
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
            val request = RentalDetailStatusModel.Request(
                status = status,
                rentalSummary = rentalSummary,
                basicRentalFee = basicRentalFee,
                deposit = rental.depositAmount,
                isPending = status == RentalStatus.PENDING,
                isAccepted = status == RentalStatus.ACCEPTED
            )
            request
        }

        RentalStatus.PAID -> {
            val daysUntilRental = daysFromToday(rental.startDate)
            RentalDetailStatusModel.Paid(
                status = status,
                rentalSummary = rentalSummary,
                daysUntilRental = daysUntilRental,
                basicRentalFee = basicRentalFee,
                deposit = rental.depositAmount,
                rentalTrackingNumber = rental.rentalTrackingNumber,
                rentalCourierName = rental.rentalCourierName,
                isSendingPhotoRegistered = rental.deliveryStatus.isPhotoRegistered,
                isSendingTrackingNumRegistered = rental.deliveryStatus.isTrackingNumberRegistered
            )
        }

        RentalStatus.RENTING -> {
            val daysFromReturnDate = daysFromToday(rental.endDate)
            val rentingStatus = RentingStatus.fromDaysFromReturnDate(daysFromReturnDate)

            RentalDetailStatusModel.Renting(
                status = rentingStatus,
                rentalSummary = rentalSummary,
                daysFromReturnDate = daysFromReturnDate,
                basicRentalFee = basicRentalFee,
                deposit = rental.depositAmount,
                rentalTrackingNumber = rental.rentalTrackingNumber,
                rentalCourierName = rental.rentalCourierName,
                returnTrackingNumber = rental.returnTrackingNumber,
                returnCourierName = rental.returnCourierName,
                isOverdue = rentingStatus == RentingStatus.RENTING_OVERDUE,
                isReturnPhotoRegistered = rental.returnStatus.isPhotoRegistered,
                isReturnTrackingNumRegistered = rental.returnStatus.isTrackingNumberRegistered,
            )
        }

        RentalStatus.RETURNED -> RentalDetailStatusModel.Returned(
            status = status,
            rentalSummary = rentalSummary,
            basicRentalFee = basicRentalFee,
            deposit = rental.depositAmount,
            rentalTrackingNumber = rental.rentalTrackingNumber,
            rentalCourierName = rental.rentalCourierName,
            returnTrackingNumber = rental.returnTrackingNumber,
            returnCourierName = rental.returnCourierName
        )
    }
}
