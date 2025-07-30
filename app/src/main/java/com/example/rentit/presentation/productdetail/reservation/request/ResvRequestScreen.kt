package com.example.rentit.presentation.productdetail.reservation.request

import android.os.Build
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray300
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.formatPrice
import com.example.rentit.navigation.productdetail.navigateToResvRequestComplete
import com.example.rentit.presentation.productdetail.reservation.request.components.DateRangePicker

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResvRequestScreen(navHostController: NavHostController, productId: Int?) {
    val resvRequestViewModel: ResvRequestViewModel = hiltViewModel()
    val context = LocalContext.current

    val rentalStartDate by resvRequestViewModel.rentalStartDate.collectAsStateWithLifecycle()
    val rentalEndDate by resvRequestViewModel.rentalEndDate.collectAsStateWithLifecycle()
    val rentalPeriod by resvRequestViewModel.rentalPeriod.collectAsStateWithLifecycle()
    val productPrice by resvRequestViewModel.productPrice.collectAsStateWithLifecycle()
    val reservedDateList by resvRequestViewModel.reservedDateList.collectAsStateWithLifecycle()

    var formattedRentalPrice by remember { mutableStateOf("") }
    val formattedTotalPrice = resvRequestViewModel.formattedTotalPrice.collectAsStateWithLifecycle()

    val sampleDeposit = 5000

    LaunchedEffect(productId) {
        if(productId == null) {
            Toast.makeText(context, context.getString(R.string.error_common_cant_find_product), Toast.LENGTH_SHORT).show()
            navHostController.popBackStack()
        } else {
            resvRequestViewModel.getProductDetail(productId)
            resvRequestViewModel.getReservedDates(productId)
        }
    }

    LaunchedEffect(rentalPeriod) {
        val totalPrice = rentalPeriod * productPrice
        formattedRentalPrice = formatPrice(totalPrice)
        resvRequestViewModel.setFormattedTotalPrice(formatPrice(totalPrice + sampleDeposit))
    }

    Scaffold(
        topBar = { CommonTopAppBar(title = stringResource(
            id = R.string.screen_resv_request_app_bar_title), onClick = {}) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DateRangePicker(
                modifier = Modifier
                    .weight(1F)
                    .padding(top = 24.dp),
                rentalStartDate = rentalStartDate,
                rentalEndDate = rentalEndDate,
                rentalPeriod = rentalPeriod,
                disabledDates = reservedDateList,
                setRentalStartDate = { date -> resvRequestViewModel.setRentalStartDate(date) },
                setRentalEndDate = { date -> resvRequestViewModel.setRentalEndDate(date) }
            )
            Column(Modifier.screenHorizontalPadding()) {
                LabelValueRow(Modifier.padding(bottom = 10.dp)) {
                    Text(text = stringResource(
                        id = R.string.screen_resv_request_label_total_rental_fee), style = MaterialTheme.typography.bodyMedium)
                    Text(text = "$formattedRentalPrice 원", style = MaterialTheme.typography.bodyMedium, color = Gray800)
                }
                LabelValueRow(Modifier.padding(bottom = 14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = stringResource(
                            id = R.string.screen_resv_request_label_deposit), style = MaterialTheme.typography.bodyMedium)
                        Icon(modifier = Modifier.padding(start = 5.dp), painter = painterResource(id = R.drawable.ic_info), contentDescription = "", tint = Gray300 )
                    }
                    Text(text = "${formatPrice(sampleDeposit)} 원",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PrimaryBlue500
                    )
                }
                CommonDivider()
                LabelValueRow(Modifier.padding(top = 14.dp)) {
                    Text(text = stringResource(
                        id = R.string.screen_resv_request_label_total_fee), style = MaterialTheme.typography.bodyLarge)
                    Text(text = "${formattedTotalPrice.value} 원", style = MaterialTheme.typography.bodyLarge, color = PrimaryBlue500)
                }
                CommonButton(text = stringResource(id = R.string.screen_resv_request_btn_resv_request),
                    containerColor = PrimaryBlue500, contentColor = Color.White, modifier = Modifier.padding(top = 21.dp)) {
                    if (rentalStartDate != null && rentalEndDate != null) {
                        resvRequestViewModel.postResv(productId ?: -1)
                    }
                }
            }
        }
    }
    ResvResultHandler(resvRequestViewModel){
        navHostController.navigateToResvRequestComplete(
            rentalStartDate = rentalStartDate.toString(),
            rentalEndDate = rentalEndDate.toString(),
            formattedTotalPrice = formattedTotalPrice.value
        )
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
fun ResvResultHandler(resvRequestViewModel: ResvRequestViewModel, onResvSuccess: () -> Unit){
    val resvResult by resvRequestViewModel.resvResult.collectAsStateWithLifecycle()
    LaunchedEffect(resvResult) {
        resvResult?.onSuccess {
            onResvSuccess()
        }?.onFailure {
            /* 예약 실패 시 */
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    RentItTheme {
        ResvRequestScreen(rememberNavController(), 0)
    }
}