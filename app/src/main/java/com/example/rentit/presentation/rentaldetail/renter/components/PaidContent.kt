package com.example.rentit.presentation.rentaldetail.renter.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.R
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.rentaldetail.common.components.LabeledSection
import com.example.rentit.presentation.rentaldetail.common.components.PriceSummary
import com.example.rentit.presentation.rentaldetail.common.components.RentalSummary
import com.example.rentit.presentation.rentaldetail.common.model.PriceItemUiModel
import com.example.rentit.presentation.rentaldetail.common.components.LabeledValue
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
    LabeledSection(
        labelText = buildAnnotatedString {
            append(stringResource(paidData.status.strRes))
            withStyle(
                style = SpanStyle(
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    color = Gray400
                )
            ) {
                append(" ")
                append(
                    stringResource(
                        R.string.screen_rental_detail_renter_paid_day_before_rent,
                        paidData.daysUntilRental
                    )
                )
            }
        },
        labelColor = paidData.status.textColor
    ) {
        RentalSummary(
            productTitle = paidData.productTitle,
            thumbnailImgUrl = paidData.thumbnailImgUrl,
            startDate = paidData.startDate,
            endDate = paidData.endDate,
            totalPrice = paidData.totalPrice,
        )

    }

    LabeledSection(labelText = AnnotatedString(stringResource(R.string.screen_rental_detail_renter_paid_price_title))) {
        PriceSummary(
            priceItems = listOf(
                PriceItemUiModel(
                    label = stringResource(R.string.screen_rental_detail_renter_paid_price_label_basic_rent),
                    price = paidData.totalPrice - paidData.deposit
                ),
                PriceItemUiModel(
                    label = stringResource(R.string.screen_rental_detail_renter_paid_price_label_deposit),
                    price = paidData.deposit
                )
            ),
            totalLabel = stringResource(R.string.screen_rental_detail_renter_paid_price_label_total)
        )
    }

    LabeledSection(labelText = AnnotatedString(stringResource(R.string.screen_rental_detail_tracking_info_title))) {
        LabeledValue(
            labelText = stringResource(R.string.screen_rental_detail_rental_tracking_num),
            value = paidData.rentalTrackingNumber
                ?: stringResource(R.string.screen_rental_detail_tracking_num_unregistered)
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val examplePendingUiModel = RentalStatusRenterUiModel.Paid(
        status = RentalStatus.PAID,
        daysUntilRental = 3,
        productTitle = "캐논 EOS 550D",
        thumbnailImgUrl = "",
        startDate = "2025-08-17",
        endDate = "2025-08-20",
        totalPrice = 10_000 * 6,
        deposit = 10_000 * 3,
        rentalTrackingNumber = null,
    )
    RentItTheme {
        Column {
            PaidContent(paidData = examplePendingUiModel)
        }
    }
}