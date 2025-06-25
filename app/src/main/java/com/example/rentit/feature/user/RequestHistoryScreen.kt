package com.example.rentit.feature.user

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.product.dto.RequestPeriodDto
import com.example.rentit.feature.product.ProductViewModel
import com.example.rentit.feature.user.component.RequestCheckCalendar
import com.example.rentit.feature.user.component.RequestHistoryListItem
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestHistoryScreen(navHostController: NavHostController, productViewModel: ProductViewModel) {
    //val requestHistory by productViewModel.requestList.collectAsStateWithLifecycle()
    val sampleRequestHistory = productViewModel.sampleReservationsList

    val requestPeriodList: List<RequestPeriodDto> = sampleRequestHistory.map { RequestPeriodDto(
        LocalDate.parse(it.startDate), LocalDate.parse(it.endDate)) }

    Scaffold(
        topBar = { CommonTopAppBar(title = "요청 내역", onClick = {}) }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            RequestCheckCalendar(requestPeriodList)
            LazyColumn(modifier = Modifier.screenHorizontalPadding(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item { Spacer(Modifier.size(2.dp)) }
                items(sampleRequestHistory) { info ->
                    RequestHistoryListItem(requestInfo = info) {}
                }
                item { Spacer(Modifier.size(50.dp)) }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewRequestHistoryScreen() {
    RentItTheme {
        //RequestHistoryScreen()
    }
}
