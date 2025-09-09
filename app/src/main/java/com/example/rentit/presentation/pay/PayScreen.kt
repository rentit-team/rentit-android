package com.example.rentit.presentation.pay

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.dialog.BaseDialog
import com.example.rentit.common.component.layout.LoadingScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.uimodel.PriceSummaryUiModel
import com.example.rentit.common.uimodel.RentalSummaryUiModel
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.rentaldetail.components.section.RentalInfoSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalPaymentSection

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PayScreen(
    rentalSummary: RentalSummaryUiModel,
    basicRentalFee: Int,
    depositAmount: Int,
    showPayResultDialog: Boolean,
    scrollState: ScrollState,
    isLoading: Boolean,
    onBackClick: () -> Unit,
    onPayClick: () -> Unit,
    onPayResultDismiss: () -> Unit,
    onPayResultConfirm: () -> Unit,
) {
    val priceItems = listOf(
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_pay_price_label_basic_rental_fee),
            price = basicRentalFee
        ),
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_pay_price_label_deposit),
            price = depositAmount
        )
    )

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.screen_pay_title)
            ) { onBackClick() }
        },
        bottomBar = {
            CommonButton(
                modifier = Modifier
                    .screenHorizontalPadding()
                    .padding(bottom = 30.dp),
                text = stringResource(R.string.screen_pay_btn_text),
                containerColor = PrimaryBlue500,
                contentColor = Color.White
            ) { onPayClick() }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(scrollState)
        ) {
            RentalInfoSection(
                title = stringResource(R.string.screen_pay_rental_info_title),
                titleColor = AppBlack,
                rentalInfo = rentalSummary,
            )

            RentalPaymentSection(
                title = stringResource(R.string.screen_pay_price_title),
                priceItems = priceItems,
                totalLabel = stringResource(R.string.screen_pay_price_label_total)
            )
            PaymentGuide()
        }
    }
    if(showPayResultDialog) PayResultDialog(onPayResultDismiss, onPayResultConfirm)

    // 로딩 상태
    // 1. 최소 로딩: 결제가 너무 빨리 완료되므로 즉시 전환 방지를 위한 UX용
    // 2. 데이터 로딩: 결제 정보를 가져올 때 표시
    LoadingScreen(isLoading)
}

@Composable
fun PaymentGuide() {

    @Composable
    fun PolicyText(@StringRes textRes: Int, hasBottomPadding: Boolean = true) {
        Text(
            modifier = if (hasBottomPadding) Modifier.padding(bottom = 4.dp) else Modifier,
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelSmall,
            color = Gray400,
            lineHeight = 15.sp
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .screenHorizontalPadding()
            .padding(vertical = 28.dp)
    ) {
        PolicyText(R.string.screen_pay_policy_title_1)
        PolicyText(R.string.screen_pay_policy_title_2, false)

        Spacer(modifier = Modifier.height(20.dp))

        PolicyText(R.string.screen_pay_policy_step_1)
        PolicyText(R.string.screen_pay_policy_step_2)
        PolicyText(R.string.screen_pay_policy_step_3, false)

        Spacer(modifier = Modifier.height(20.dp))

        PolicyText(R.string.screen_pay_policy_tip_1)
        PolicyText(R.string.screen_pay_policy_tip_2, false)
    }
}

@Composable
fun PayResultDialog(
    onClose: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    BaseDialog(
        title = stringResource(R.string.dialog_pay_result_success_title),
        confirmBtnText = stringResource(R.string.dialog_pay_result_btn_confirm),
        isBackgroundClickable = false,
        onDismissRequest = onClose,
        onConfirmRequest = onConfirm,
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val sample = RentalSummaryUiModel(
            productTitle = "캐논 EOS 550D",
            thumbnailImgUrl = "",
            startDate = "2025-08-17",
            endDate = "2025-08-20",
            totalPrice = 10_000 * 6
        )

    RentItTheme {
        PayScreen(
            showPayResultDialog = false,
            scrollState = rememberScrollState(),
            isLoading = false,
            onBackClick = { },
            onPayClick = { },
            onPayResultDismiss = { },
            onPayResultConfirm = { },
            rentalSummary = sample,
            basicRentalFee = 10000,
            depositAmount = 5000,
        )
    }
}