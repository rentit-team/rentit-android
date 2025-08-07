package com.example.rentit.presentation.rentaldetail.renter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.rentit.data.rental.dto.ChangedBy
import com.example.rentit.data.rental.dto.DeliveryStatus
import com.example.rentit.data.rental.dto.Product
import com.example.rentit.data.rental.dto.Rental
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.data.rental.dto.Renter
import com.example.rentit.data.rental.dto.ReturnStatus
import com.example.rentit.data.rental.dto.StatusHistory
import com.example.rentit.presentation.rentaldetail.renter.model.RentalStatusRenterUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RentalDetailRenterViewModel @Inject constructor(
): ViewModel() {
    private val sampleRentalDetail = RentalDetailResponseDto(
        rental = Rental(
            reservationId = 1001,
            renter = Renter(
                userId = 101,
                nickname = "김철수"
            ),
            status = "RENTING",
            product = Product(
                title = "캐논 EOS R6 카메라",
                thumbnailImgUrl = "https://example.com/image/123.jpg"
            ),
            startDate = "2025-04-15",
            endDate = "2025-04-17",
            totalAmount = 90000,
            depositAmount = 45000,
            rentalTrackingNumber = "1234567890",
            returnTrackingNumber = null,
            deliveryStatus = DeliveryStatus(
                isPhotoRegistered = true,
                isTrackingNumberRegistered = true
            ),
            returnStatus = ReturnStatus(
                isPhotoRegistered = false,
                isTrackingNumberRegistered = false
            )
        ),
        statusHistory = listOf(
            StatusHistory(
                status = "PENDING",
                changedAt = "2025-03-25T09:00:00Z",
                changedBy = ChangedBy(
                    userId = 1,
                    nickname = "김숙명"
                )
            ),
            StatusHistory(
                status = "ACCEPTED",
                changedAt = "2025-03-25T10:00:00Z",
                changedBy = ChangedBy(
                    userId = 2,
                    nickname = "홍길동"
                )
            ),
            StatusHistory(
                status = "COMPLETED",
                changedAt = "2025-03-25T10:30:00Z",
                changedBy = ChangedBy(
                    userId = 1,
                    nickname = "김숙명"
                )
            ),
            StatusHistory(
                status = "RENTING",
                changedAt = "2025-04-15T00:00:00Z",
                changedBy = ChangedBy(
                    userId = 0,
                    nickname = "SYSTEM"
                )
            )
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private val _uiModel = MutableStateFlow(sampleRentalDetail.toUiModel())

    @RequiresApi(Build.VERSION_CODES.O)
    val uiModel: StateFlow<RentalStatusRenterUiModel> = _uiModel

}