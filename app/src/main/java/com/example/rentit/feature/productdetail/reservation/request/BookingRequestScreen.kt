package com.example.rentit.feature.productdetail.reservation.request

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rentit.R
import com.example.rentit.common.component.CommonButton
import com.example.rentit.common.component.CommonDivider
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.component.moveScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray300
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.feature.productdetail.ProductViewModel
import com.example.rentit.feature.productdetail.reservation.request.components.DateRangePicker
import java.text.NumberFormat
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BookingRequestScreen(navHostController: NavHostController, productViewModel: ProductViewModel) {

    val productId by productViewModel.productId.collectAsStateWithLifecycle()
    val bookingStartDate = productViewModel.bookingStartDate.collectAsStateWithLifecycle()
    val bookingEndDate = productViewModel.bookingEndDate.collectAsStateWithLifecycle()
    val rentalPeriod by productViewModel.bookingPeriod.collectAsStateWithLifecycle()

    val productDetailResult by productViewModel.productDetail.collectAsStateWithLifecycle()
    val productPrice = productDetailResult?.getOrNull()?.product?.price ?: 0

    val sampleDeposit = 5000

    val numFormat = NumberFormat.getNumberInstance()
    var formattedRentalPrice by remember { mutableStateOf("") }
    val formattedTotalPrice = productViewModel.formattedTotalPrice.collectAsStateWithLifecycle()

    LaunchedEffect(rentalPeriod) {
        val totalPrice = rentalPeriod * productPrice
        formattedRentalPrice = numFormat.format(totalPrice)
        productViewModel.setFormattedTotalPrice(numFormat.format(totalPrice + sampleDeposit))
    }

    Scaffold(
        topBar = { CommonTopAppBar(title = stringResource(
            id = R.string.screen_booking_request_app_bar_title), onClick = {}) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DateRangePicker(
                productViewModel, yearMonth = YearMonth.now(), modifier = Modifier
                    .weight(1F)
                    .padding(top = 24.dp)
            )
            Column(Modifier.screenHorizontalPadding()) {
                LabelValueRow(Modifier.padding(bottom = 10.dp)) {
                    Text(text = stringResource(
                        id = R.string.screen_booking_request_label_total_rental_fee), style = MaterialTheme.typography.bodyMedium)
                    Text(text = "$formattedRentalPrice 원", style = MaterialTheme.typography.bodyMedium, color = Gray800)
                }
                LabelValueRow(Modifier.padding(bottom = 14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(
                            id = R.string.screen_booking_request_label_deposit), style = MaterialTheme.typography.bodyMedium)
                        Icon(modifier = Modifier.padding(start = 5.dp), painter = painterResource(id = R.drawable.ic_info), contentDescription = "", tint = Gray300 )
                    }
                    Text(text = "${numFormat.format(sampleDeposit)} 원",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PrimaryBlue500
                    )
                }
                CommonDivider()
                LabelValueRow(Modifier.padding(top = 14.dp)) {
                    Text(text = stringResource(
                        id = R.string.screen_booking_request_label_total_fee), style = MaterialTheme.typography.bodyLarge)
                    Text(text = "${formattedTotalPrice.value} 원", style = MaterialTheme.typography.bodyLarge, color = PrimaryBlue500)
                }
                CommonButton(text = stringResource(id = R.string.screen_booking_request_btn_booking_request),
                    containerColor = PrimaryBlue500, contentColor = Color.White, modifier = Modifier.padding(top = 21.dp)) {
                    val startDate = bookingStartDate.value
                    val endDate = bookingEndDate.value
                    if (startDate != null && endDate != null) {
                        productViewModel.postBooking(
                            productId,
                            startDate = startDate,
                            endDate = endDate
                        )
                    }
                }
            }
        }
    }
    BookingResultHandler(productViewModel){
        moveScreen(navHostController, NavigationRoutes.REQUESTCONFIRM)
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

@Composable
fun BookingResultHandler(productViewModel: ProductViewModel, onBookingSuccess: () -> Unit){
    val bookingResult by productViewModel.bookingResult.collectAsStateWithLifecycle()
    LaunchedEffect(bookingResult) {
        bookingResult?.onSuccess {
            onBookingSuccess()
        }?.onFailure {
            /* 예약 실패 시 */
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewBookingRequestScreen() {
    RentItTheme {
        BookingRequestScreen(rememberNavController(), hiltViewModel())
    }
}