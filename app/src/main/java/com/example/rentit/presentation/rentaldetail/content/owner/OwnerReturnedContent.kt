package com.example.rentit.presentation.rentaldetail.content.owner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.ArrowedTextButton
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.uimodel.PriceSummaryUiModel
import com.example.rentit.presentation.rentaldetail.components.section.RentalPaymentSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalInfoSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalTrackingSection
import com.example.rentit.common.uimodel.RentalSummaryUiModel
import com.example.rentit.domain.rental.model.RentalDetailStatusModel

/**
 * 대여 상세(대여자)에서
 * 반납 완료 완료 상태를 나타내는 UI 컨텐츠
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OwnerReturnedContent(
    returnedData: RentalDetailStatusModel.Returned,
    onCheckPhotoClick: () -> Unit = {},
    onRentalSummaryClick: () -> Unit = {}
) {
    val priceItems = listOf(
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_rental_detail_owner_total_profit_price_label_basic_rent),
            price = returnedData.basicRentalFee
        ),
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_rental_detail_owner_total_profit_price_label_deposit),
            price = returnedData.deposit
        )
    )

    RentalInfoSection(
        title = stringResource(returnedData.status.strRes),
        titleColor = returnedData.status.color,
        rentalInfo = returnedData.rentalSummary,
        onRentalSummaryClick = onRentalSummaryClick
    ) {
        ArrowedTextButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .offset(y = 8.dp),
            text = stringResource(R.string.screen_rental_detail_returned_check_photo_btn),
            onClick = onCheckPhotoClick
        )
    }

    RentalPaymentSection(
        title = stringResource(R.string.screen_rental_detail_owner_total_profit_price_title),
        priceItems = priceItems,
        totalLabel = stringResource(R.string.screen_rental_detail_owner_total_profit_price_label_total)
    )

    RentalTrackingSection(
        rentalTrackingNumber = returnedData.rentalTrackingNumber,
        rentalCourierName = returnedData.rentalCourierName,
        returnTrackingNumber = returnedData.returnTrackingNumber,
        returnCourierName = returnedData.returnCourierName
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val examplePendingUiModel = RentalDetailStatusModel.Returned(
        status = RentalStatus.RETURNED,
        rentalSummary = RentalSummaryUiModel(
            productTitle = "프리미엄 캠핑 텐트",
            thumbnailImgUrl = "https://example.com/images/tent_thumbnail.jpg",
            startDate = "2025-08-10",
            endDate = "2025-08-14",
            totalPrice = 120_000
        ),
        basicRentalFee = 90_000,
        deposit = 10_000 * 3,
        rentalTrackingNumber = null,
        returnTrackingNumber = null,
        rentalCourierName = null,
        returnCourierName = null
    )
    RentItTheme {
        Column {
            OwnerReturnedContent(returnedData = examplePendingUiModel)
        }
    }
}