package com.example.rentit.presentation.rentaldetail.content.owner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.uimodel.PriceSummaryUiModel
import com.example.rentit.presentation.rentaldetail.components.section.RentalPaymentSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalInfoSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalTrackingSection
import com.example.rentit.common.uimodel.RentalSummaryUiModel
import com.example.rentit.presentation.rentaldetail.components.ArrowedTextButton
import com.example.rentit.presentation.rentaldetail.components.NoticeBanner
import com.example.rentit.presentation.rentaldetail.components.section.RentalTaskSection
import com.example.rentit.domain.rental.model.RentalDetailStatusModel

/**
 * 대여 상세(대여자)에서
 * 상품 발송 준비(사용자 결제 완료) 상태를 나타내는 UI 컨텐츠
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OwnerPaidContent(
    paidData: RentalDetailStatusModel.Paid,
    onPhotoTaskClick: () -> Unit = {},
    onTrackingNumTaskClick: () -> Unit = {},
    onCancelRentClick: () -> Unit = {},
) {
    val priceItem = listOf(
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_rental_detail_owner_expected_price_label_basic_rent),
            price = paidData.basicRentalFee
        )
    )

    NoticeBanner(noticeText = AnnotatedString(stringResource(R.string.screen_rental_detail_owner_paid_notice_complete_requirement)))

    RentalInfoSection(
        title = stringResource(R.string.rental_status_paid_owner),
        titleColor = paidData.status.textColor,
        subTitle = stringResource(
            R.string.screen_rental_detail_subtitle_day_before_rent,
            paidData.daysUntilRental
        ),
        subTitleColor = Gray400,
        rentalInfo = paidData.rentalSummary,
    )

    RentalTaskSection(
        title = stringResource(R.string.screen_rental_detail_owner_paid_sending_task_title),
        guideText = stringResource(R.string.screen_rental_detail_owner_paid_sending_task_info),
        photoTaskLabel = stringResource(R.string.screen_rental_detail_owner_paid_sending_task_photo),
        trackingNumTaskLabel = stringResource(R.string.screen_rental_detail_owner_paid_sending_task_tracking_num),
        isTaskAvailable = true,
        isPhotoRegistered = paidData.isSendingPhotoRegistered,
        isTrackingNumRegistered = paidData.isSendingTrackingNumRegistered,
        onPhotoTaskClick = onPhotoTaskClick,
        onTrackingNumTaskClick = onTrackingNumTaskClick
    )

    RentalPaymentSection(
        title = stringResource(R.string.screen_rental_detail_owner_expected_price_title),
        priceItems = priceItem,
        totalLabel = stringResource(R.string.screen_rental_detail_owner_expected_price_label_total)
    )

    RentalTrackingSection(
        rentalTrackingNumber = paidData.rentalTrackingNumber,
        rentalCourierName = paidData.rentalCourierName
    )

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
        ArrowedTextButton(
            modifier = Modifier.padding(vertical = 10.dp),
            text = stringResource(R.string.screen_rental_detail_request_btn_cancel_rent),
            onClick = onCancelRentClick
        )
    }
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
        rentalTrackingNumber = null,
        isSendingPhotoRegistered = false,
        isSendingTrackingNumRegistered = false,
        deposit = 10_000,
        rentalCourierName = "롯데택배",
    )
    RentItTheme {
        Column {
            OwnerPaidContent(paidData = examplePendingUiModel)
        }
    }
}