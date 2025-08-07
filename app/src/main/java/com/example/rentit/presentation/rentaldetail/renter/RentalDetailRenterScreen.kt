package com.example.rentit.presentation.rentaldetail.renter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.rentit.data.rental.dto.DeliveryStatus
import com.example.rentit.data.rental.dto.Product
import com.example.rentit.data.rental.dto.Rental
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.data.rental.dto.Renter
import com.example.rentit.data.rental.dto.ReturnStatus
import com.example.rentit.presentation.rentaldetail.common.dialog.UnknownStatusDialog
import com.example.rentit.presentation.rentaldetail.renter.components.PaidContent
import com.example.rentit.presentation.rentaldetail.renter.components.RentalRequestContent
import com.example.rentit.presentation.rentaldetail.renter.components.RentingContent
import com.example.rentit.presentation.rentaldetail.renter.components.ReturnedContent
import com.example.rentit.presentation.rentaldetail.renter.model.RentalStatusRenterUiModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalDetailRenterScreen(navHostController: NavHostController, uiModel: RentalStatusRenterUiModel, scrollState: ScrollState) {
    Scaffold(
        topBar = { CommonTopAppBar(title = stringResource(R.string.screen_rental_detail_title), navHostController = navHostController) }
    ) {
        Column(modifier = Modifier.padding(it).verticalScroll(scrollState)) {
            when(uiModel) {
                is RentalStatusRenterUiModel.Request ->
                    RentalRequestContent(uiModel)
                is RentalStatusRenterUiModel.Paid ->
                    PaidContent(uiModel)
                is RentalStatusRenterUiModel.Renting ->
                    RentingContent(uiModel)
                is RentalStatusRenterUiModel.Returned ->
                    ReturnedContent(uiModel)
                is RentalStatusRenterUiModel.Unknown ->
                    UnknownStatusDialog { navHostController.popBackStack() }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val sample1 = RentalDetailResponseDto(
        rental = Rental(
            reservationId = 1001,
            renter = Renter(userId = 101, nickname = "김철수"),
            status = "RENTING",
            product = Product(
                title = "캐논 EOS R6 카메라",
                thumbnailImgUrl = "https://example.com/image/123.jpg"
            ),
            startDate = "2025-08-02",
            endDate = "2025-08-05",
            totalAmount = 90000,
            depositAmount = 45000,
            rentalTrackingNumber = "1234567890",
            returnTrackingNumber = null,
            deliveryStatus = DeliveryStatus(isPhotoRegistered = true, isTrackingNumberRegistered = true),
            returnStatus = ReturnStatus(isPhotoRegistered = false, isTrackingNumberRegistered = false)
        ),
        statusHistory = listOf(/* 생략 */)
    )

    val sample2 = sample1.copy(
        rental = sample1.rental.copy(
            endDate = "2025-08-04",
            returnStatus = ReturnStatus(isPhotoRegistered = false, isTrackingNumberRegistered = false)
        )
    )

    val sample3 = sample1.copy(
        rental = sample1.rental.copy(
            endDate = "2025-08-01",
            returnStatus = ReturnStatus(isPhotoRegistered = true, isTrackingNumberRegistered = false)
        )
    )

    val sample4 = sample1.copy(
        rental = sample1.rental.copy(
            endDate = "2025-07-30",
            returnStatus = ReturnStatus(isPhotoRegistered = true, isTrackingNumberRegistered = true)
        )
    )

    val sample5 = sample1.copy(
        rental = sample1.rental.copy(
            status = "RETURNED",
            endDate = "2025-07-28",
            returnStatus = ReturnStatus(isPhotoRegistered = false, isTrackingNumberRegistered = true)
        )
    )

    RentItTheme {
        RentalDetailRenterScreen(
            navHostController = rememberNavController(),
            sample5.toUiModel(),
            rememberScrollState()
        )
    }
}