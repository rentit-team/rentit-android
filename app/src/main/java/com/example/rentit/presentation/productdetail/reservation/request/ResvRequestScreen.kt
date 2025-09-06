package com.example.rentit.presentation.productdetail.reservation.request

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonDivider
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.paddingForBottomBarButton
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray300
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.formatPrice
import com.example.rentit.presentation.productdetail.reservation.request.components.DateRangePicker
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResvRequestScreen(
    rentalStartDate: LocalDate? = null,
    rentalEndDate: LocalDate? = null,
    rentalPeriod: Int = 0,
    reservedDateList: List<String> = emptyList(),
    rentalPrice: Int = 0,
    totalPrice: Int = 0,
    deposit: Int = 0,
    onBackClick: () -> Unit,
    onResvRequestClick: () -> Unit,
    onSetRentalStartDate: (LocalDate?) -> Unit,
    onSetRentalEndDate: (LocalDate?) -> Unit,
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(id = R.string.screen_resv_request_app_bar_title),
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            CommonButton(
                modifier = Modifier
                    .screenHorizontalPadding()
                    .paddingForBottomBarButton(),
                text = stringResource(id = R.string.screen_resv_request_btn_resv_request),
                containerColor = PrimaryBlue500,
                contentColor = Color.White,
                onClick = onResvRequestClick
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DateRangePicker(
                modifier = Modifier.padding(top = 10.dp, bottom = 26.dp),
                rentalStartDate = rentalStartDate,
                rentalEndDate = rentalEndDate,
                rentalPeriod = rentalPeriod,
                disabledDates = reservedDateList,
                setRentalStartDate = onSetRentalStartDate,
                setRentalEndDate = onSetRentalEndDate
            )
            PriceSection(
                rentalPrice = rentalPrice,
                deposit = deposit,
                totalPrice = totalPrice
            )
        }
    }
}

@Composable
fun PriceSection(rentalPrice: Int, deposit: Int, totalPrice: Int){
    Column(Modifier.screenHorizontalPadding()) {
        LabelValueRow(Modifier.padding(bottom = 10.dp)) {
            Text(
                text = stringResource(
                    id = R.string.screen_resv_request_label_total_rental_fee
                ), style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${formatPrice(rentalPrice)} 원",
                style = MaterialTheme.typography.bodyMedium,
                color = Gray800
            )
        }
        LabelValueRow(Modifier.padding(bottom = 14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.screen_resv_request_label_deposit),
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    modifier = Modifier.padding(start = 5.dp),
                    painter = painterResource(id = R.drawable.ic_info),
                    contentDescription = "",
                    tint = Gray300
                )
            }
            Text(
                text = "${formatPrice(deposit)} 원",
                style = MaterialTheme.typography.bodyMedium,
                color = PrimaryBlue500
            )
        }
        CommonDivider()
        LabelValueRow(Modifier.padding(top = 14.dp)) {
            Text(
                text = stringResource(
                    id = R.string.screen_resv_request_label_total_fee
                ), style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "${formatPrice(totalPrice)} 원",
                style = MaterialTheme.typography.bodyLarge,
                color = PrimaryBlue500
            )
        }
    }
}

@Composable
fun LabelValueRow(modifier: Modifier, content: @Composable () -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        content()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ResvRequestScreenPreview() {
    RentItTheme {
        ResvRequestScreen(
            rentalStartDate = LocalDate.now(),
            rentalEndDate = LocalDate.now(),
            rentalPeriod = 5, // 대여일수
            reservedDateList = listOf("2025-09-12", "2025-09-13"), // 이미 예약된 날짜
            rentalPrice = 90_000,
            totalPrice = 120_000,
            deposit = 30_000,
            onBackClick = { /* 뒤로가기 처리 */ },
            onResvRequestClick = { /* 예약 요청 처리 */ },
            onSetRentalStartDate = { /* 시작일 선택 처리 */ },
            onSetRentalEndDate = { /* 종료일 선택 처리 */ }
        )
    }
}