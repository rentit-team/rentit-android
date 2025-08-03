package com.example.rentit.presentation.rentaldetail.renter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rentit.R
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.rental.dto.ChangedBy
import com.example.rentit.data.rental.dto.DeliveryStatus
import com.example.rentit.data.rental.dto.Product
import com.example.rentit.data.rental.dto.Rental
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.data.rental.dto.Renter
import com.example.rentit.data.rental.dto.ReturnStatus
import com.example.rentit.data.rental.dto.StatusHistory
import com.example.rentit.presentation.rentaldetail.renter.components.RentalRequestContent
import com.example.rentit.presentation.rentaldetail.renter.model.RentalStatusRenterUiModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalDetailRenterView(navHostController: NavHostController, uiModel: RentalStatusRenterUiModel) {
    Scaffold(
        topBar = { CommonTopAppBar(title = stringResource(R.string.screen_rental_detail_title), navHostController = navHostController) }
    ) {
        Column(modifier = Modifier.padding(it)) {
            when(uiModel) {
                is RentalStatusRenterUiModel.Request ->
                    RentalRequestContent(uiModel)
                is RentalStatusRenterUiModel.Paid -> {}

                is RentalStatusRenterUiModel.Renting -> {}

                is RentalStatusRenterUiModel.Returned -> {}

                is RentalStatusRenterUiModel.Unknown -> {}
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val sampleRentalDetail = RentalDetailResponseDto(
        rental = Rental(
            reservationId = 1001,
            renter = Renter(
                userId = 101,
                nickname = "김철수"
            ),
            status = "ACCEPTED",
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

    RentItTheme {
        RentalDetailRenterView(
            navHostController = rememberNavController(),
            sampleRentalDetail.toUiModel()
        )
    }
}