package com.example.rentit.presentation.rentaldetail.renter.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.R
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.rentaldetail.common.model.PriceItemUiModel
import com.example.rentit.presentation.rentaldetail.common.components.section.PaymentInfoSection
import com.example.rentit.presentation.rentaldetail.common.components.section.RentalInfoSection
import com.example.rentit.presentation.rentaldetail.common.components.section.TrackingInfoSection
import com.example.rentit.presentation.rentaldetail.common.model.RentalSummaryUiModel
import com.example.rentit.presentation.rentaldetail.renter.model.RentalStatusRenterUiModel

/**
 * 대여 상세(판매자)에서
 * 결제 완료 상태를 나타내는 UI 컨텐츠
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaidContent(
    paidData: RentalStatusRenterUiModel.Paid,
) {
    val priceItems = listOf(
        PriceItemUiModel(
            label = stringResource(R.string.screen_rental_detail_renter_paid_price_label_basic_rent),
            price = paidData.basicRentalFee
        ),
        PriceItemUiModel(
            label = stringResource(R.string.screen_rental_detail_renter_paid_price_label_deposit),
            price = paidData.deposit
        )
    )

    RentalInfoSection(
        title = stringResource(paidData.status.strRes),
        titleColor = paidData.status.textColor,
        subTitle = stringResource(
            R.string.screen_rental_detail_renter_paid_day_before_rent,
            paidData.daysUntilRental
        ),
        subTitleColor = Gray400,
        rentalInfo = paidData.rentalSummary,
    )

    PaymentInfoSection(
        title = stringResource(R.string.screen_rental_detail_renter_paid_price_title),
        priceItems = priceItems,
        totalLabel = stringResource(R.string.screen_rental_detail_renter_paid_price_label_total)
    )

    TrackingInfoSection(
        rentalTrackingNumber = paidData.rentalTrackingNumber,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val examplePendingUiModel = RentalStatusRenterUiModel.Paid(
        status = RentalStatus.PAID,
        daysUntilRental = 3,
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
    )
    RentItTheme {
        Column {
            PaidContent(paidData = examplePendingUiModel)
        }
    }
}