package com.example.rentit.feature.user

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.user.dto.RequestInfoDto
import com.example.rentit.feature.user.component.RequestCheckCalendar
import com.example.rentit.feature.user.component.RequestHistoryListItem

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RequestHistoryScreen() {
    val requestHistory = emptyList<RequestInfoDto>()

    Scaffold(
        topBar = { CommonTopAppBar(title = "요청 내역", onClick = {}) }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            RequestCheckCalendar()
            LazyColumn {
                items(requestHistory) {
                    RequestHistoryListItem(requestInfo = it)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewRequestHistoryScreen() {
    RentItTheme {
        RequestHistoryScreen()
    }
}
