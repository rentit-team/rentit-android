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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.model.PriceSummaryUiModel
import com.example.rentit.common.model.RentalSummaryUiModel
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.presentation.pay.payresult.PayResultDialog
import com.example.rentit.presentation.pay.payresult.PayResultDialogUiModel
import com.example.rentit.presentation.rentaldetail.components.section.RentalInfoSection
import com.example.rentit.presentation.rentaldetail.components.section.RentalPaymentSection

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PayScreen(
    navHostController: NavHostController,
    payModel: PayUiModel,
    dialogModel: PayResultDialogUiModel,
    isDialogVisible: Boolean,
    scrollState: ScrollState,
    onPayClick: () -> Unit,
    onDialogClose: () -> Unit,
    onDialogConfirm: () -> Unit,
) {
    val priceItems = listOf(
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_pay_price_label_basic_rental_fee),
            price = payModel.basicRentalFee
        ),
        PriceSummaryUiModel(
            label = stringResource(R.string.screen_pay_price_label_deposit),
            price = payModel.deposit
        )
    )

    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.screen_pay_title)
            ) { navHostController.popBackStack() }
        },
        bottomBar = {
            CommonButton(
                stringResource(R.string.screen_pay_btn_text),
                PrimaryBlue500,
                Color.White,
                Modifier
                    .screenHorizontalPadding()
                    .padding(bottom = 30.dp),
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
                rentalInfo = payModel.rentalSummary,
            )

            RentalPaymentSection(
                title = stringResource(R.string.screen_pay_price_title),
                priceItems = priceItems,
                totalLabel = stringResource(R.string.screen_pay_price_label_total)
            )

            PaymentGuide()
        }
    }
    if(isDialogVisible) PayResultDialog(dialogModel, onDialogClose, onDialogConfirm)
}

@Composable
fun PaymentGuide() {

    @Composable
    fun PolicyText(@StringRes textRes: Int, hasBottomPadding: Boolean = true) {
        Text(
            modifier = if (hasBottomPadding) Modifier.padding(bottom = 4.dp) else Modifier,
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelSmall,
            color = Gray400
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    val sample = PayUiModel(
        rentalSummary = RentalSummaryUiModel(
            productTitle = "캐논 EOS 550D",
            thumbnailImgUrl = "",
            startDate = "2025-08-17",
            endDate = "2025-08-20",
            totalPrice = 10_000 * 6
        ),
        basicRentalFee = 90_000,
        deposit = 10_000 * 3
    )
    val sampleDialog = PayResultDialogUiModel(
        titleText = stringResource(R.string.dialog_pay_result_success_title),
        contentText = stringResource(R.string.dialog_pay_result_failed_content)
    )

    RentItTheme {
        PayScreen(
            navHostController = rememberNavController(),
            payModel = sample,
            dialogModel = sampleDialog,
            isDialogVisible = false,
            scrollState = rememberScrollState(),
            onPayClick = { },
            onDialogClose = { },
            onDialogConfirm = { }
        )
    }
}