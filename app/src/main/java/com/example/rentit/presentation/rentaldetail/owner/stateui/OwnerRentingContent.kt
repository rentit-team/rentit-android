package com.example.rentit.presentation.rentaldetail.owner.stateui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.uimodel.PriceSummaryUiModel
import com.example.rentit.presentation.rentaldetail.components.section.RentalPaymentSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalInfoSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalTrackingSection
import com.example.rentit.common.uimodel.RentalSummaryUiModel
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.util.formatPrice
import com.example.rentit.presentation.rentaldetail.model.RentingStatus
import kotlin.math.abs

/**
 * 대여 상세(대여자)에서
 * 대여중 상태(대여중, 반납 전, 반납 지연)를 나타내는 UI 컨텐츠
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OwnerRentingContent(
    rentingData: OwnerRentalStatusUiModel.Renting,
) {
    val priceItem = listOf(
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_rental_detail_owner_expected_price_label_basic_rent),
            price = rentingData.basicRentalFee
        )
    )

    val formattedDeposit = formatPrice(rentingData.deposit)

    RentalInfoSection(
        title = stringResource(rentingData.status.strRes),
        titleColor = rentingData.status.textColor,
        subTitle = rentingData.status.subLabelStrRes?.let {
            stringResource(it, abs(rentingData.daysFromReturnDate))
        },
        rentalInfo = rentingData.rentalSummary,
    ) {
        if (rentingData.isOverdue) {
            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = buildAnnotatedString {
                    append(stringResource(R.string.screen_rental_detail_owner_renting_overdue_info_reward_leading_text))
                    withStyle(style = SpanStyle(color = PrimaryBlue500)) {
                        append(" ${formattedDeposit}${stringResource(R.string.common_price_unit)}")
                    }
                    append(stringResource(R.string.screen_rental_detail_owner_renting_overdue_info_reward_tail_text))
                },
                style = MaterialTheme.typography.labelMedium
            )
        }
    }

    RentalPaymentSection(
        title = stringResource(R.string.screen_rental_detail_owner_expected_price_title),
        priceItems = priceItem,
        totalLabel = stringResource(R.string.screen_rental_detail_owner_expected_price_label_total)
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
        status = RentingStatus.RENTING_OVERDUE,
        isOverdue = true,
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
    )
    RentItTheme {
        Column {
            OwnerRentingContent(rentingData = examplePendingUiModel)
        }
    }
}