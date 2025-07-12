package com.example.rentit.feature.productdetail.reservation.request.complete

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonDivider
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.component.moveScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResvRequestCompleteScreen(
    navHostController: NavHostController,
    rentalStartDate: String,
    rentalEndDate: String,
    rentalPeriod: Int,
    formattedTotalPrice: String,
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .screenHorizontalPadding()
            .padding(bottom = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painter = painterResource(id = R.drawable.ic_check_circle), contentDescription = stringResource(
            id = R.string.content_description_for_ic_check), tint = PrimaryBlue500 )
        Text(modifier = Modifier.padding(vertical = 34.dp), text = stringResource(
            id = R.string.screen_request_confirm_title), style = MaterialTheme.typography.headlineLarge)
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(
                id = R.string.screen_request_confirm_resv_period), style = MaterialTheme.typography.bodyLarge)
            Text(text = "$rentalStartDate ~ $rentalEndDate · $rentalPeriod 일", style = MaterialTheme.typography.bodyMedium, color = Gray800)
        }
        CommonDivider()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = stringResource(
                id = R.string.screen_request_confirm_total_price), style = MaterialTheme.typography.bodyLarge)
            Text(text = "$formattedTotalPrice 원",
                style = MaterialTheme.typography.bodyLarge,
                color = PrimaryBlue500)
        }
        CommonButton(text = "완료", containerColor = Gray100, contentColor = AppBlack, modifier = Modifier.padding(top = 52.dp)) {
            moveScreen(navHostController, NavigationRoutes.MAIN)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    RentItTheme {
        ResvRequestCompleteScreen(
            rememberNavController(),
            rentalStartDate = "2025-07-12",
            rentalEndDate = "2025-07-16",
            rentalPeriod = 5,
            formattedTotalPrice = "45,000"
        )
    }
}