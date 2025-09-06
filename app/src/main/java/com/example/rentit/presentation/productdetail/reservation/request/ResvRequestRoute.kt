package com.example.rentit.presentation.productdetail.reservation.request

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.common.util.formatPrice
import com.example.rentit.navigation.productdetail.navigateToResvRequestComplete

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResvRequestRoute(navHostController: NavHostController, productId: Int) {
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
        resvRequestViewModel.getProductDetail(productId)
        resvRequestViewModel.getReservedDates(productId)
    }

    LaunchedEffect(rentalPeriod) {
        val totalPrice = rentalPeriod * productPrice
        formattedRentalPrice = formatPrice(totalPrice)
        resvRequestViewModel.setFormattedTotalPrice(formatPrice(totalPrice + sampleDeposit))
    }

    navHostController.navigateToResvRequestComplete(
        rentalStartDate = rentalStartDate.toString(),
        rentalEndDate = rentalEndDate.toString(),
        formattedTotalPrice = formattedTotalPrice.value
    )

    ResvRequestScreen(
        rentalStartDate = TODO(),
        rentalEndDate = TODO(),
        rentalPeriod = TODO(),
        reservedDateList = TODO(),
        rentalPrice = TODO(),
        totalPrice = TODO(),
        deposit = TODO(),
        onBackClick = TODO(),
        onResvRequestClick = TODO(),
        onSetRentalStartDate = TODO(),
        onSetRentalEndDate = TODO()
    ) 
}