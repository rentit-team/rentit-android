package com.example.rentit.presentation.rentaldetail.renter.components

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
import com.example.rentit.R
import com.example.rentit.common.theme.AppRed
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.rentaldetail.common.components.LabeledSection
import com.example.rentit.presentation.rentaldetail.common.components.PriceSummary
import com.example.rentit.presentation.rentaldetail.common.components.RentalSummary
import com.example.rentit.presentation.rentaldetail.common.model.PriceItemUiModel
import com.example.rentit.presentation.rentaldetail.common.components.LabeledValue
import com.example.rentit.presentation.rentaldetail.common.components.NoticeBanner
import com.example.rentit.presentation.rentaldetail.common.components.TaskCheckBox
import com.example.rentit.presentation.rentaldetail.renter.model.RentalStatusRenterUiModel
import com.example.rentit.presentation.rentaldetail.renter.model.RentingStatus
import kotlin.math.abs

/**
 * 대여 상세(판매자)에서
 * 대여중 상태(대여중, 반납 전, 반납 지연)를 나타내는 UI 컨텐츠
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentingContent(
    rentingData: RentalStatusRenterUiModel.Renting,
) {
    val returnRegCountText = listOf(
        rentingData.isReturnPhotoRegistered,
        rentingData.isReturnTrackingNumRegistered
    ).count { it }.let { "($it/2)" }

    if (rentingData.status.noticeBannerStrRes != null)
        NoticeBanner(noticeText = AnnotatedString(stringResource(rentingData.status.noticeBannerStrRes)))

    LabeledSection(
        labelText = buildAnnotatedString {
            append(stringResource(rentingData.status.strRes))
            if (rentingData.status.subLabelStrRes != null)
                withStyle(style = SpanStyle(color = Gray400)) {
                    append(
                        " " + stringResource(
                            rentingData.status.subLabelStrRes,
                            abs(rentingData.daysFromReturnDate)
                        )
                    )
                }
        },
        labelColor = rentingData.status.textColor
    ) {
        RentalSummary(
            productTitle = rentingData.productTitle,
            startDate = rentingData.startDate,
            endDate = rentingData.endDate,
            totalPrice = rentingData.totalPrice,
        )
    }

    LabeledSection(
        labelText = buildAnnotatedString {
            append(stringResource(R.string.screen_rental_detail_renter_return_task_title) + " $returnRegCountText")
        }
    ) {
        if (rentingData.isOverdue) ReturnOverdueWarning(
            rentingData.daysFromReturnDate,
            rentingData.deposit
        )
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = stringResource(R.string.screen_rental_detail_renter_return_task_info),
            style = MaterialTheme.typography.labelMedium
        )
        TaskCheckBox(
            taskText = stringResource(R.string.screen_rental_detail_renter_return_task_photo),
            isTaskEnable = rentingData.isReturnAvailable,
            isDone = rentingData.isReturnPhotoRegistered,
        )
        TaskCheckBox(
            taskText = stringResource(R.string.screen_rental_detail_renter_return_task_tracking_num),
            isTaskEnable = rentingData.isReturnAvailable,
            isDone = rentingData.isReturnTrackingNumRegistered
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = stringResource(R.string.screen_rental_detail_renter_return_task_policy),
            style = MaterialTheme.typography.labelSmall,
            color = Gray400
        )
    }

    LabeledSection(labelText = AnnotatedString(stringResource(R.string.screen_rental_detail_renter_paid_price_title))) {
        PriceSummary(
            priceItems = listOf(
                PriceItemUiModel(
                    label = stringResource(R.string.screen_rental_detail_renter_paid_price_label_basic_rent),
                    price = rentingData.totalPrice - rentingData.deposit
                ),
                PriceItemUiModel(
                    label = stringResource(R.string.screen_rental_detail_renter_paid_price_label_deposit),
                    price = rentingData.deposit
                )
            ),
            totalLabel = stringResource(R.string.screen_rental_detail_renter_paid_price_label_total)
        )
    }

    LabeledSection(labelText = AnnotatedString(stringResource(R.string.screen_rental_detail_tracking_info_title))) {
        LabeledValue(
            labelText = stringResource(R.string.screen_rental_detail_rental_tracking_num),
            value = rentingData.rentalTrackingNumber
                ?: stringResource(R.string.screen_rental_detail_tracking_num_unregistered)
        )
    }
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
            textAlign = TextAlign.Center
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val examplePendingUiModel = RentalStatusRenterUiModel.Renting(
        status = RentingStatus.RENTING_RETURN_DAY,
        isOverdue = false,
        daysFromReturnDate = 3,
        productTitle = "캐논 EOS 550D",
        thumbnailImgUrl = "",
        startDate = "2025-08-17",
        endDate = "2025-08-20",
        totalPrice = 10_000 * 6,
        deposit = 10_000 * 3,
        rentalTrackingNumber = null,
        isReturnAvailable = false,
        isReturnPhotoRegistered = true,
        isReturnTrackingNumRegistered = false,
    )
    RentItTheme {
        Column {
            RentingContent(rentingData = examplePendingUiModel)
        }
    }
}