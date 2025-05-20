package com.example.rentit.feature.product

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.calendar.CommonCalendar
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray300
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingRequestScreen() {

    Scaffold(
        topBar = { CommonTopAppBar(title = stringResource(
            id = R.string.screen_booking_request_app_bar_title), onClick = {}) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(bottom = 64.dp)
                .screenHorizontalPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CommonCalendar(yearMonth = YearMonth.now(), modifier = Modifier.weight(1F).padding(top = 30.dp))
            Text(modifier = Modifier.fillMaxWidth().padding(bottom = 26.dp),
                text = "시작일 ~ 종료일 · 기간 일",
                style = MaterialTheme.typography.bodyMedium,
                color = Gray800)
            LabelValueRow(Modifier.padding(bottom = 10.dp)) {
                Text(text = stringResource(
                    id = R.string.screen_booking_request_label_total_rental_fee), style = MaterialTheme.typography.bodyMedium)
                Text(text = "가격 원", style = MaterialTheme.typography.bodyMedium, color = Gray800)
            }
            LabelValueRow(Modifier.padding(bottom = 14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(
                        id = R.string.screen_booking_request_label_deposit), style = MaterialTheme.typography.bodyMedium)
                    Icon(modifier = Modifier.padding(start = 5.dp), painter = painterResource(id = R.drawable.ic_info), contentDescription = "", tint = Gray300 )
                }
                Text(text = "가격 원",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PrimaryBlue500
                )
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray100))
            LabelValueRow(Modifier.padding(top = 14.dp)) {
                Text(text = stringResource(
                    id = R.string.screen_booking_request_label_total_fee), style = MaterialTheme.typography.bodyLarge)
                Text(text = "가격 원", style = MaterialTheme.typography.bodyLarge, color = PrimaryBlue500)
            }
            CommonButton(text = stringResource(id = R.string.screen_booking_request_btn_booking_request),
                containerColor = PrimaryBlue500, contentColor = Color.White, modifier = Modifier.padding(top = 21.dp)) {}
        }
    }
}

@Composable
fun LabelValueRow(modifier: Modifier, content: @Composable () -> Unit) {
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        content()
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewBookingRequestScreen() {
    RentItTheme {
        BookingRequestScreen()
    }
}