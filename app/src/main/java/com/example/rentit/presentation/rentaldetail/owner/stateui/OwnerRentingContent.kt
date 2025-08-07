package com.example.rentit.presentation.rentaldetail.owner.stateui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.R
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.model.PriceSummaryUiModel
import com.example.rentit.presentation.rentaldetail.components.NoticeBanner
import com.example.rentit.presentation.rentaldetail.components.section.RentalPaymentSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalInfoSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalTaskSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalTrackingSection
import com.example.rentit.common.model.RentalSummaryUiModel
import com.example.rentit.presentation.rentaldetail.model.RentingStatus
import com.example.rentit.presentation.rentaldetail.renter.stateui.RenterRentalStatusUiModel
import kotlin.math.abs

/**
 * 대여 상세(판매자)에서
 * 대여중 상태(대여중, 반납 전, 반납 지연)를 나타내는 UI 컨텐츠
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OwnerRentingContent(
    rentingData: OwnerRentalStatusUiModel.Renting,
) {
    val priceItems = listOf(
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_rental_detail_renter_charge_price_label_basic_rent),
            price = rentingData.basicRentalFee
        ),
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_rental_detail_renter_charge_price_label_deposit),
            price = rentingData.deposit
        )
    )

    if (rentingData.status.noticeBannerStrRes != null) {
        NoticeBanner(noticeText = AnnotatedString(stringResource(rentingData.status.noticeBannerStrRes)))
    }

    RentalInfoSection(
        title = stringResource(rentingData.status.strRes),
        titleColor = rentingData.status.textColor,
        subTitle = rentingData.status.subLabelStrRes?.let {
            stringResource( it, abs(rentingData.daysFromReturnDate))
        },
        rentalInfo = rentingData.rentalSummary,
    )

    RentalTaskSection(
        title = stringResource(R.string.screen_rental_detail_renter_return_task_title),
        guideText = stringResource(R.string.screen_rental_detail_renter_return_task_info),
        policyText = stringResource(R.string.screen_rental_detail_renter_return_task_policy),
        photoTaskLabel = stringResource(R.string.screen_rental_detail_renter_return_task_photo),
        trackingNumTaskLabel = stringResource(R.string.screen_rental_detail_renter_return_task_tracking_num),
        isReturnAvailable = rentingData.isReturnAvailable,
        isPhotoRegistered = rentingData.isReturnPhotoRegistered,
        isTrackingNumRegistered = rentingData.isReturnTrackingNumRegistered
    ) { }

    RentalPaymentSection(
        title = stringResource(R.string.screen_rental_detail_renter_paid_price_title),
        priceItems = priceItems,
        totalLabel = stringResource(R.string.screen_rental_detail_renter_paid_price_label_total)
    )

    RentalTrackingSection(
        rentalTrackingNumber = rentingData.rentalTrackingNumber,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val examplePendingUiModel = OwnerRentalStatusUiModel.Renting(
        status = RentingStatus.RENTING_RETURN_DAY,
        isOverdue = false,
        daysFromReturnDate = 3,
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
        isReturnAvailable = false,
        isReturnPhotoRegistered = true,
        isReturnTrackingNumRegistered = false,
    )
    RentItTheme {
        Column {
            OwnerRentingContent(rentingData = examplePendingUiModel)
        }
    }
}