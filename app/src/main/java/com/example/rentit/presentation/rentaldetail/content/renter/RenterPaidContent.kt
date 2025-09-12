package com.example.rentit.presentation.rentaldetail.content.renter

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
import com.example.rentit.common.uimodel.PriceSummaryUiModel
import com.example.rentit.presentation.rentaldetail.components.section.RentalPaymentSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalInfoSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalTrackingSection
import com.example.rentit.common.uimodel.RentalSummaryUiModel
import com.example.rentit.domain.rental.model.RentalDetailStatusModel

/**
 * 대여 상세(사용자)에서
 * 결제 완료 상태를 나타내는 UI 컨텐츠
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RenterPaidContent(
    paidData: RentalDetailStatusModel.Paid,
    onRentalSummaryClick: () -> Unit = {}
) {
    val priceItems = listOf(
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_rental_detail_renter_paid_price_label_basic_rent),
            price = paidData.basicRentalFee
        ),
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_rental_detail_renter_paid_price_label_deposit),
            price = paidData.deposit
        )
    )

    RentalInfoSection(
        title = stringResource(paidData.status.strRes),
        titleColor = paidData.status.color,
        subTitle = stringResource(
            R.string.screen_rental_detail_subtitle_day_before_rent,
            paidData.daysUntilRental
        ),
        subTitleColor = Gray400,
        rentalInfo = paidData.rentalSummary,
        onRentalSummaryClick = onRentalSummaryClick
    )

    RentalPaymentSection(
        title = stringResource(R.string.screen_rental_detail_renter_paid_price_title),
        priceItems = priceItems,
        totalLabel = stringResource(R.string.screen_rental_detail_renter_paid_price_label_total)
    )

    RentalTrackingSection(
        rentalTrackingNumber = paidData.rentalTrackingNumber,
        rentalCourierName = paidData.rentalCourierName,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val examplePendingUiModel = RentalDetailStatusModel.Paid(
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
        isSendingPhotoRegistered = false,
        isSendingTrackingNumRegistered = false,
        rentalCourierName = null,
    )
    RentItTheme {
        Column {
            RenterPaidContent(paidData = examplePendingUiModel)
        }
    }
}