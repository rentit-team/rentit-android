package com.example.rentit.presentation.rentaldetail.content.owner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.ui.component.item.RentItAnimatedNoticeBanner
import com.example.rentit.common.ui.component.item.RentItArrowedTextButton
import com.example.rentit.common.ui.extension.rentItScreenHorizontalPadding
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.rentaldetail.model.PriceSummaryUiModel
import com.example.rentit.presentation.rentaldetail.components.section.RentalPaymentSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalInfoSection
import com.example.rentit.presentation.rentaldetail.model.RentalSummaryUiModel
import com.example.rentit.domain.rental.model.RentalDetailStatusModel

/**
 * 대여 상세(대여자)에서
 * 요청과 관련된 상태(대여 요청, 요청 수락, 요청 거절, 거래 취소)를 나타내는 UI 컨텐츠
 */

@Composable
fun OwnerRequestContent(
    requestData: RentalDetailStatusModel.Request,
    onRequestResponseClick: () -> Unit = {},
    onCancelRentClick: () -> Unit = {},
    onRentalSummaryClick: () -> Unit = {}
) {
    val priceItem = listOf(
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_rental_detail_owner_expected_price_label_basic_rent),
            price = requestData.basicRentalFee
        )
    )

    if(requestData.isPending || requestData.isAccepted) {
        NoticeBannerSection(requestData.isPending, requestData.isAccepted)
    }

    RentalInfoSection(
        title = stringResource(requestData.status.strRes),
        titleColor = requestData.status.color,
        rentalInfo = requestData.rentalSummary,
        onRentalSummaryClick = onRentalSummaryClick
    ) {
        if(requestData.isPending) {
            RentItArrowedTextButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .offset(y = 8.dp),
                text = stringResource(R.string.screen_rental_detail_owner_request_btn_request_response),
                onClick = onRequestResponseClick
            )
        }
    }

    RentalPaymentSection(
        title = stringResource(R.string.screen_rental_detail_owner_expected_price_title),
        priceItems = priceItem,
        totalLabel = stringResource(R.string.screen_rental_detail_owner_expected_price_label_total)
    )

    if(requestData.isAccepted) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
            RentItArrowedTextButton(
                modifier = Modifier.padding(vertical = 10.dp),
                text = stringResource(R.string.screen_rental_detail_request_btn_cancel_rent),
                onClick = onCancelRentClick
            )
        }
    }
}

@Composable
fun NoticeBannerSection(isPending: Boolean, isAccepted: Boolean) {
    val noticeText = buildAnnotatedString {
        when {
            isPending -> {
                append(stringResource(R.string.screen_rental_detail_owner_request_notice_new_request_1))
                withStyle(style = SpanStyle(color = PrimaryBlue500)) {
                    append(" " + stringResource(R.string.screen_rental_detail_owner_request_notice_new_request_2))
                }
                append(stringResource(R.string.screen_rental_detail_owner_request_notice_new_request_3))
            }

            isAccepted -> {
                append(stringResource(R.string.screen_rental_detail_owner_request_notice_pay_1))
                withStyle(style = SpanStyle(color = PrimaryBlue500)) {
                    append(" " + stringResource(R.string.screen_rental_detail_owner_request_notice_pay_2))
                }
                append(stringResource(R.string.screen_rental_detail_owner_request_notice_pay_3))
            }

            else -> {}
        }
    }

    RentItAnimatedNoticeBanner(modifier = Modifier.rentItScreenHorizontalPadding(), noticeText = noticeText)
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    val examplePendingUiModel = RentalDetailStatusModel.Request(
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
        deposit = 10_000,
    )
    RentItTheme {
        Column {
            OwnerRequestContent(requestData = examplePendingUiModel)
        }
    }
}