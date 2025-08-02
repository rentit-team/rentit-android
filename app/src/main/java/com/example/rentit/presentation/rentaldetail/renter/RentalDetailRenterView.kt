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
import com.example.rentit.data.product.dto.RentalDetailDto
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
    val sampleRentalDetail = RentalDetailDto(
        rentalId = 101L,
        renterId = 202L,
        renterNickname = "렌팃",
        sellerId = 303L,
        rentalStatus = "ACCEPTED",
        productImageUrl = "https://example.com/images/camera.jpg",
        productTitle = "캐논 DSLR 카메라",
        startDate = "2025-07-25",
        endDate = "2025-07-28",
        pricePerDay = 20000,
        rentalTrackingNumber = "1234567890ABC",
        returnTrackingNumber = null, // 아직 반납 전
        isShipped = true,
        isReturned = false,
        overdueDays = 0
    )
    RentItTheme {
        RentalDetailRenterView(
            navHostController = rememberNavController(),
            sampleRentalDetail.toUiModel()
        )
    }
}