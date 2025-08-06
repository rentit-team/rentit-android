package com.example.rentit.presentation.rentaldetail.renter.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
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
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.rentaldetail.common.components.LabeledSection
import com.example.rentit.presentation.rentaldetail.common.components.PriceSummary
import com.example.rentit.presentation.rentaldetail.common.components.RentalSummary
import com.example.rentit.presentation.rentaldetail.common.PriceItemUiModel
import com.example.rentit.presentation.rentaldetail.common.components.ArrowedTextButton
import com.example.rentit.presentation.rentaldetail.common.components.LabeledValue
import com.example.rentit.presentation.rentaldetail.renter.model.RentalStatusRenterUiModel

/**
 * 대여 상세(판매자)에서
 * 반납 완료 완료 상태를 나타내는 UI 컨텐츠
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReturnedContent(
    returnedData: RentalStatusRenterUiModel.Returned,
) {
    LabeledSection(
        labelText = AnnotatedString(stringResource(returnedData.status.strRes)),
        labelColor = returnedData.status.textColor
    ) {
        RentalSummary(
            productTitle = returnedData.productTitle,
            thumbnailImgUrl = returnedData.thumbnailImgUrl,
            startDate = returnedData.startDate,
            endDate = returnedData.endDate,
            totalPrice = returnedData.totalPrice,
        )
        ArrowedTextButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .offset(y = 8.dp),
            text = stringResource(R.string.screen_rental_detail_renter_returned_check_photo_btn)
        ) { }
    }

    LabeledSection(labelText = AnnotatedString(stringResource(R.string.screen_rental_detail_renter_total_paid_price_title))) {
        PriceSummary(
            priceItems = listOf(
                PriceItemUiModel(
                    label = stringResource(R.string.screen_rental_detail_renter_total_paid_price_label_basic_rent),
                    price = returnedData.totalPrice - returnedData.deposit
                ),
                PriceItemUiModel(
                    label = stringResource(R.string.screen_rental_detail_renter_total_paid_price_label_deposit),
                    price = returnedData.deposit
                )
            ),
            totalLabel = stringResource(R.string.screen_rental_detail_renter_total_paid_price_label_total)
        )
    }

    LabeledSection(labelText = AnnotatedString(stringResource(R.string.screen_rental_detail_tracking_info_title))) {
        LabeledValue(
            modifier = Modifier.padding(bottom = 10.dp),
            labelText = stringResource(R.string.screen_rental_detail_rental_tracking_num),
            value = returnedData.rentalTrackingNumber
                ?: stringResource(R.string.screen_rental_detail_tracking_num_unregistered)
        )
        LabeledValue(
            labelText = stringResource(R.string.screen_rental_detail_returned_tracking_num),
            value = returnedData.returnTrackingNumber
                ?: stringResource(R.string.screen_rental_detail_tracking_num_unregistered)
        )
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val examplePendingUiModel = RentalStatusRenterUiModel.Returned(
        status = RentalStatus.RETURNED,
        productTitle = "캐논 EOS 550D",
        thumbnailImgUrl = "",
        startDate = "2025-08-17",
        endDate = "2025-08-20",
        totalPrice = 10_000 * 6,
        deposit = 10_000 * 3,
        rentalTrackingNumber = null,
        returnTrackingNumber = null,
    )
    RentItTheme {
        Column {
            ReturnedContent(returnedData = examplePendingUiModel)
        }
    }
}