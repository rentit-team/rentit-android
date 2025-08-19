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
import com.example.rentit.R
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.rental.dto.DeliveryStatusDto
import com.example.rentit.data.rental.dto.ProductDto
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.data.rental.dto.RentalDto
import com.example.rentit.data.rental.dto.RenterDto
import com.example.rentit.data.rental.dto.ReturnStatusDto
import com.example.rentit.presentation.rentaldetail.dialog.UnknownStatusDialog
import com.example.rentit.presentation.rentaldetail.renter.stateui.RenterPaidContent
import com.example.rentit.presentation.rentaldetail.renter.stateui.RenterRequestContent
import com.example.rentit.presentation.rentaldetail.renter.stateui.RenterRentalStatusUiModel
import com.example.rentit.presentation.rentaldetail.renter.stateui.RenterRentingContent
import com.example.rentit.presentation.rentaldetail.renter.stateui.RenterReturnedContent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalDetailRenterScreen(uiModel: RenterRentalStatusUiModel, scrollState: ScrollState, onBackClick: () -> Unit) {
    Scaffold(
        topBar = { CommonTopAppBar(title = stringResource(R.string.screen_rental_detail_title)) { onBackClick() } }
    ) {
        Column(modifier = Modifier.padding(it).verticalScroll(scrollState)) {
            when(uiModel) {
                is RenterRentalStatusUiModel.Request ->
                    RenterRequestContent(uiModel)
                is RenterRentalStatusUiModel.Paid ->
                    RenterPaidContent(uiModel)
                is RenterRentalStatusUiModel.Renting ->
                    RenterRentingContent(uiModel)
                is RenterRentalStatusUiModel.Returned ->
                    RenterReturnedContent(uiModel)
                is RenterRentalStatusUiModel.Unknown ->
                    UnknownStatusDialog { onBackClick() }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val sample1 = RentalDetailResponseDto(
        rental = RentalDto(
            reservationId = 1001,
            renter = RenterDto(userId = 101, nickname = "김철수"),
            status = "RENTING",
            product = ProductDto(
                title = "캐논 EOS R6 카메라",
                thumbnailImgUrl = "https://example.com/image/123.jpg"
            ),
            startDate = "2025-08-02",
            endDate = "2025-08-05",
            totalAmount = 90000,
            depositAmount = 45000,
            rentalTrackingNumber = "1234567890",
            returnTrackingNumber = null,
            deliveryStatus = DeliveryStatusDto(isPhotoRegistered = true, isTrackingNumberRegistered = true),
            returnStatus = ReturnStatusDto(isPhotoRegistered = false, isTrackingNumberRegistered = false)
        ),
        statusHistory = listOf(/* 생략 */)
    )

    val sample2 = sample1.copy(
        rental = sample1.rental.copy(
            status = "RETURNED",
            endDate = "2025-07-28",
            returnStatus = ReturnStatusDto(isPhotoRegistered = false, isTrackingNumberRegistered = true)
        )
    )

    RentItTheme {
        RentalDetailRenterScreen(
            sample2.toRenterUiModel(),
            rememberScrollState()
        ) {}
    }
}