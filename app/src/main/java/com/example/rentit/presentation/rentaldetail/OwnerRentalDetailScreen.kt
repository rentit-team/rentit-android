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
import com.example.rentit.presentation.rentaldetail.content.owner.OwnerPaidContent
import com.example.rentit.presentation.rentaldetail.content.owner.OwnerRentingContent
import com.example.rentit.presentation.rentaldetail.content.owner.OwnerRequestContent
import com.example.rentit.presentation.rentaldetail.content.owner.OwnerReturnedContent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OwnerRentalDetailScreen(
    uiModel: RentalDetailStatusModel,
    scrollState: ScrollState,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onRequestResponseClick: () -> Unit,
    onCancelRentClick: () -> Unit,
    onPhotoTaskClick: () -> Unit,
    onTrackingNumTaskClick: () -> Unit,
    onCheckPhotoClick: () -> Unit,
) {
    Scaffold(
        topBar = { CommonTopAppBar(title = stringResource(R.string.screen_rental_detail_title)) { onBackClick() } }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .verticalScroll(scrollState)) {
            when(uiModel) {
                is RentalDetailStatusModel.Request ->
                    OwnerRequestContent(
                        requestData = uiModel,
                        onRequestResponseClick = onRequestResponseClick,
                        onCancelRentClick = onCancelRentClick
                    )
                is RentalDetailStatusModel.Paid ->
                    OwnerPaidContent(
                        paidData = uiModel,
                        onPhotoTaskClick = onPhotoTaskClick,
                        onTrackingNumTaskClick = onTrackingNumTaskClick,
                        onCancelRentClick = onCancelRentClick
                    )
                is RentalDetailStatusModel.Renting ->
                    OwnerRentingContent(uiModel)
                is RentalDetailStatusModel.Returned ->
                    OwnerReturnedContent(
                        returnedData = uiModel,
                        onCheckPhotoClick = onCheckPhotoClick
                    )
                is RentalDetailStatusModel.Unknown -> Unit
            }
        }
        LoadingScreen(isLoading)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun OwnerRentalDetailScreenPreview() {
    val sample1 = RentalDetailResponseDto(
        rental = RentalDto(
            reservationId = 1001,
            renter = RenterDto(userId = 101, nickname = "김철수"),
            status = RentalStatus.PENDING,
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
        OwnerRentalDetailScreen(
            sample2.toModel(),
            rememberScrollState(),
            isLoading = true,
            onBackClick = {},
            onRequestResponseClick = {},
            onCancelRentClick = {},
            onPhotoTaskClick = {},
            onTrackingNumTaskClick = {},
        ) {}
    }
}