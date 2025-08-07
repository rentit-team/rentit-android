package com.example.rentit.presentation.rentaldetail.owner.stateui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.model.PriceSummaryUiModel
import com.example.rentit.presentation.rentaldetail.components.ArrowedTextButton
import com.example.rentit.presentation.rentaldetail.components.NoticeBanner
import com.example.rentit.presentation.rentaldetail.components.section.RentalPaymentSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalInfoSection
import com.example.rentit.common.model.RentalSummaryUiModel

/**
 * 대여 상세(대여자)에서
 * 요청과 관련된 상태(대여 요청, 요청 수락, 요청 거절, 거래 취소)를 나타내는 UI 컨텐츠
 */

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OwnerRequestContent(
    requestData: OwnerRentalStatusUiModel.Request,
) {
    val priceItem = listOf(
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_rental_detail_owner_expected_price_label_basic_rent),
            price = requestData.basicRentalFee
        )
    )

    when {
        requestData.isPending -> NoticeBanner(noticeText = buildAnnotatedString {
            append(stringResource(R.string.screen_rental_detail_owner_request_notice_new_request))
        })
        requestData.isAccepted -> NoticeBanner(noticeText = buildAnnotatedString {
            withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {
                append(stringResource(R.string.screen_rental_detail_owner_request_notice_pay))
            }
            append(stringResource(R.string.screen_rental_detail_owner_request_notice_wait))
        })
    }

    RentalInfoSection(
        title = stringResource(requestData.status.strRes),
        titleColor = requestData.status.textColor,
        rentalInfo = requestData.rentalSummary,
    ) {
        if(requestData.isPending) {
            ArrowedTextButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .offset(y = 8.dp),
                text = stringResource(R.string.screen_rental_detail_owner_request_btn_request_response)
            ) { }
        }
    }

    RentalPaymentSection(
        title = stringResource(R.string.screen_rental_detail_owner_expected_price_title),
        priceItems = priceItem,
        totalLabel = stringResource(R.string.screen_rental_detail_owner_expected_price_label_total)
    )

    if(requestData.isAccepted) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            ArrowedTextButton(
                modifier = Modifier.padding(vertical = 10.dp),
                text = stringResource(R.string.screen_rental_detail_request_btn_cancel_rent)
            ) { }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val examplePendingUiModel = OwnerRentalStatusUiModel.Request(
        status = RentalStatus.REJECTED,
        isPending = false,
        isAccepted = false,
        rentalSummary = RentalSummaryUiModel(
            productTitle = "프리미엄 캠핑 텐트",
            thumbnailImgUrl = "https://example.com/images/tent_thumbnail.jpg",
            startDate = "2025-08-10",
            endDate = "2025-08-14",
            totalPrice = 120_000
        ),
        basicRentalFee = 90_000,
        deposit = 10_000 * 3
    )
    RentItTheme {
        Column {
            OwnerRequestContent(requestData = examplePendingUiModel)
        }
    }
}