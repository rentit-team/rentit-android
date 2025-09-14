package com.example.rentit.presentation.rentaldetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.ExtendedFAB
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
    onBackClick: () -> Unit,
    onTransactionReceiptClick: () -> Unit,
    onPayClick: () -> Unit,
    onCancelRentClick: () -> Unit,
    onTrackingNumTaskClick: () -> Unit,
    onPhotoTaskClick: () -> Unit,
    onCheckPhotoClick: () -> Unit,
    onRentalSummaryClick: () -> Unit,
    onChattingClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CommonTopAppBar (
                title = stringResource(R.string.screen_rental_detail_title),
                showTransactionReceipt = true,
                onBackClick = onBackClick,
                onTransactionReceiptClick = onTransactionReceiptClick
            )
        },
        floatingActionButton = {
            ExtendedFAB(
                modifier = Modifier.padding(bottom = 16.dp),
                iconRes = R.drawable.ic_chat,
                textRes = R.string.screen_rental_detail_chat_floating_button,
                onClick = onChattingClick
            )
        }
    ) {
        Column(modifier = Modifier.padding(it).verticalScroll(scrollState)) {
            when(uiModel) {
                is RentalDetailStatusModel.Request ->
                    RenterRequestContent(uiModel, onPayClick, onCancelRentClick, onRentalSummaryClick)
                is RentalDetailStatusModel.Paid ->
                    RenterPaidContent(uiModel, onRentalSummaryClick)
                is RentalDetailStatusModel.Renting ->
                    RenterRentingContent(uiModel, onPhotoTaskClick, onTrackingNumTaskClick, onRentalSummaryClick)
                is RentalDetailStatusModel.Returned ->
                    RenterReturnedContent(uiModel, onCheckPhotoClick, onRentalSummaryClick)
                is RentalDetailStatusModel.Unknown -> Unit
            }
            Spacer(modifier = Modifier.height(100.dp))
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
            chatroomId = null,
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
            onBackClick =  { },
            onTransactionReceiptClick = { },
            onPayClick = { },
            onCancelRentClick = { },
            onTrackingNumTaskClick = { },
            onPhotoTaskClick = { },
            onCheckPhotoClick = { },
            onRentalSummaryClick = { },
            onChattingClick = { }
        )
    }
}