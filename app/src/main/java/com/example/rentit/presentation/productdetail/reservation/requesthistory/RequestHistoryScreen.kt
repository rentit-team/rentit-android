package com.example.rentit.presentation.productdetail.reservation.requesthistory

import android.os.Build
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.R
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.navigation.NavigationRoutes
import com.example.rentit.common.navigation.moveScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.product.dto.RequestInfoDto
import com.example.rentit.data.product.dto.RequestPeriodDto
import com.example.rentit.presentation.productdetail.reservation.requesthistory.components.RequestCalendar
import com.example.rentit.presentation.productdetail.reservation.requesthistory.components.RequestHistoryListItem
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestHistoryScreen(navHostController: NavHostController, productId: Int?) {
    val requestHistoryViewModel: RequestHistoryViewModel = hiltViewModel()
    val requestHistory by requestHistoryViewModel.requestList.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var yearMonth by remember { mutableStateOf(YearMonth.now()) }
    val errorMsgNewChatRoom = LocalContext.current.getString(R.string.error_mypage_new_chatroom)

    val requestPeriodList: List<RequestPeriodDto> = requestHistory.map {
        RequestPeriodDto(
            LocalDate.parse(it.startDate), LocalDate.parse(it.endDate)
        )
    }

    val groupedByMonth: Map<YearMonth, List<RequestInfoDto>> = requestHistory.groupBy {
        YearMonth.from(LocalDate.parse(it.startDate))
    }

    LaunchedEffect(productId) {
        if(productId == null){
            Toast.makeText(context, context.getString(R.string.error_common_cant_find_product), Toast.LENGTH_SHORT).show()
            navHostController.popBackStack()
        } else {
            requestHistoryViewModel.getProductRequestList(productId)
        }
    }

    Scaffold(
        topBar = { CommonTopAppBar(title = "요청 내역", onClick = {}) }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            RequestCalendar(requestPeriodList) { month -> yearMonth = month }
            LazyColumn(
                modifier = Modifier.screenHorizontalPadding(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item { Spacer(Modifier.size(2.dp)) }
                items(groupedByMonth[yearMonth] ?: emptyList()) { info ->
                    RequestHistoryListItem(requestInfo = info) {
                        if (info.chatRoomId != null) {
                            moveScreen(
                                navHostController,
                                "${NavigationRoutes.NAVHOSTCHAT}/$productId/${info.reservationId}/${info.chatRoomId}"
                            )
                        } else {
                            requestHistoryViewModel.postNewChat(
                                productId = productId ?: -1,
                                onSuccess = { chatRoomId ->
                                    moveScreen(
                                        navHostController,
                                        "${NavigationRoutes.NAVHOSTCHAT}/$productId/${info.reservationId}/$chatRoomId"
                                    )
                                },
                                onError = {
                                    Toast.makeText(navHostController.context, errorMsgNewChatRoom, Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
                item { Spacer(Modifier.size(50.dp)) }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    RentItTheme {
        //RequestHistoryScreen()
    }
}
