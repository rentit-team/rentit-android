package com.example.rentit.presentation.productdetail.rentalhistory

import android.os.Build
import android.widget.Toast
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
import com.example.rentit.R
import com.example.rentit.data.product.dto.RequestPeriodDto
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryRoute(navHostController: NavHostController, productId: Int) {
    val viewModel: RentalHistoryViewModel = hiltViewModel()
    val context = LocalContext.current
    val requestHistory by viewModel.requestList.collectAsStateWithLifecycle()

    var yearMonth by remember { mutableStateOf(YearMonth.now()) }

    val requestPeriodList: List<RequestPeriodDto> = requestHistory.map {
        RequestPeriodDto(
            LocalDate.parse(it.startDate), LocalDate.parse(it.endDate)
        )
    }

    LaunchedEffect(productId) {
        viewModel.getProductRequestList(productId)
    }

    RentalHistoryScreen(
        requestPeriodList = requestPeriodList,
        onChangeMonth = { yearMonth = it },
        onBackClick = navHostController::popBackStack
    )
}
