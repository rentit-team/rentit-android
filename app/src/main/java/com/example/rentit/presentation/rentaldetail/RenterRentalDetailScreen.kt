package com.example.rentit.presentation.rentaldetail

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
import com.example.rentit.common.component.layout.LoadingScreen
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.rental.dto.DeliveryStatusDto
import com.example.rentit.data.rental.dto.ProductDto
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.data.rental.dto.RentalDto
import com.example.rentit.data.rental.dto.RenterDto
import com.example.rentit.data.rental.dto.ReturnStatusDto
import com.example.rentit.data.rental.mapper.toModel
import com.example.rentit.domain.rental.model.RentalDetailStatusModel
import com.example.rentit.presentation.rentaldetail.content.renter.RenterPaidContent
import com.example.rentit.presentation.rentaldetail.content.renter.RenterRequestContent
import com.example.rentit.presentation.rentaldetail.content.renter.RenterRentingContent
import com.example.rentit.presentation.rentaldetail.content.renter.RenterReturnedContent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalDetailRenterScreen(
    uiModel: RentalDetailStatusModel,
    scrollState: ScrollState,
    isLoading: Boolean,
    onBackPressed: () -> Unit,
    onPayClick: () -> Unit,
    onCancelRentClick: () -> Unit,
    onTrackingNumTaskClick: () -> Unit,
    onPhotoTaskClick: () -> Unit,
    onCheckPhotoClick: () -> Unit,
) {
    Scaffold(
        topBar = { CommonTopAppBar(title = stringResource(R.string.screen_rental_detail_title), onBackClick = onBackPressed) }
    ) {
        Column(modifier = Modifier.padding(it).verticalScroll(scrollState)) {
            when(uiModel) {
                is RentalDetailStatusModel.Request ->
                    RenterRequestContent(uiModel, onPayClick, onCancelRentClick)
                is RentalDetailStatusModel.Paid ->
                    RenterPaidContent(uiModel)
                is RentalDetailStatusModel.Renting ->
                    RenterRentingContent(uiModel, onPhotoTaskClick, onTrackingNumTaskClick)
                is RentalDetailStatusModel.Returned ->
                    RenterReturnedContent(uiModel, onCheckPhotoClick)
                is RentalDetailStatusModel.Unknown -> Unit
            }
        }
        LoadingScreen(isLoading)
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
            status = RentalStatus.RENTING,
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
            deliveryStatus = DeliveryStatusDto(
                isPhotoRegistered = true,
                isTrackingNumberRegistered = true
            ),
            returnStatus = ReturnStatusDto(
                isPhotoRegistered = false,
                isTrackingNumberRegistered = false
            ),
            rentalCourierName = null,
            returnCourierName = null
        ),
        statusHistory = listOf(/* 생략 */)
    )

    val sample2 = sample1.copy(
        rental = sample1.rental.copy(
            status = RentalStatus.RETURNED,
            endDate = "2025-07-28",
            returnStatus = ReturnStatusDto(isPhotoRegistered = false, isTrackingNumberRegistered = true)
        )
    )

    RentItTheme {
        RentalDetailRenterScreen(
            uiModel = sample2.toModel(),
            scrollState = rememberScrollState(),
            isLoading = true,
            onBackPressed =  { },
            onPayClick = { },
            onCancelRentClick = { },
            onTrackingNumTaskClick = { },
            onPhotoTaskClick = { },
            onCheckPhotoClick = { }
        )
    }
}