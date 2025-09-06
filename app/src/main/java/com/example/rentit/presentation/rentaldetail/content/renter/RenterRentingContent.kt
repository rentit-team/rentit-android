package com.example.rentit.presentation.rentaldetail.content.renter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentit.R
import com.example.rentit.common.theme.AppRed
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.uimodel.PriceSummaryUiModel
import com.example.rentit.presentation.rentaldetail.components.NoticeBanner
import com.example.rentit.presentation.rentaldetail.components.section.RentalPaymentSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalInfoSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalTaskSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalTrackingSection
import com.example.rentit.common.uimodel.RentalSummaryUiModel
import com.example.rentit.domain.rental.model.RentalDetailStatusModel
import com.example.rentit.domain.rental.model.RentingStatus
import kotlin.math.abs

/**
 * 대여 상세(사용자)에서
 * 대여중 상태(대여중, 반납 전, 반납 지연)를 나타내는 UI 컨텐츠
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RenterRentingContent(
    rentingData: RentalDetailStatusModel.Renting,
    onPhotoTaskClick: () -> Unit = {},
    onTrackingNumTaskClick: () -> Unit = {},
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
        isTaskAvailable = rentingData.isReturnAvailable,
        isPhotoRegistered = rentingData.isReturnPhotoRegistered,
        isTrackingNumRegistered = rentingData.isReturnTrackingNumRegistered,
        onPhotoTaskClick = onPhotoTaskClick,
        onTrackingNumTaskClick = onTrackingNumTaskClick,
    ) { if (rentingData.isOverdue) {
            ReturnOverdueWarning(
                rentingData.daysFromReturnDate,
                rentingData.deposit
            )
        }
    }

    RentalPaymentSection(
        title = stringResource(R.string.screen_rental_detail_renter_paid_price_title),
        priceItems = priceItems,
        totalLabel = stringResource(R.string.screen_rental_detail_renter_paid_price_label_total)
    )

    RentalTrackingSection(
        rentalTrackingNumber = rentingData.rentalTrackingNumber,
        rentalCourierName = rentingData.rentalCourierName,
        returnTrackingNumber = rentingData.returnTrackingNumber,
        returnCourierName = rentingData.returnCourierName
    )
}

@Composable
fun ReturnOverdueWarning(daysFromReturnDate: Int, deposit: Int) {
    Box(
        modifier = Modifier
            .padding(bottom = 18.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Gray100),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = buildAnnotatedString {
                append(stringResource(R.string.screen_rental_detail_renter_renting_overdue_warning_leading_text) + " ")
                withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {
                    append(abs(daysFromReturnDate).toString() + stringResource(R.string.screen_rental_detail_renter_renting_overdue_warning_day_highlight))
                }
                append(stringResource(R.string.screen_rental_detail_renter_renting_overdue_warning_middle_text) + " ")
                withStyle(style = SpanStyle(color = AppRed)) {
                    append(deposit.toString() + stringResource(R.string.common_price_unit))
                }
                append(stringResource(R.string.screen_rental_detail_renter_renting_overdue_warning_tail_text))
            },
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val examplePendingUiModel = RentalDetailStatusModel.Renting(
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
        rentalCourierName = null,
        returnTrackingNumber = null,
        returnCourierName = null
    )
    RentItTheme {
        Column {
            RenterRentingContent(rentingData = examplePendingUiModel)
        }
    }
}