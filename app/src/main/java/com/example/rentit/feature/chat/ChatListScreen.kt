package com.example.rentit.feature.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rentit.R
import com.example.rentit.common.component.FilterButton
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.component.moveScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.feature.chat.component.ChatListItem

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListScreen(navHostController: NavHostController) {
    Column(Modifier
        .fillMaxSize()
        .padding(top = 45.dp)) {
        Column(
            modifier = Modifier
                .screenHorizontalPadding()
        ) {
            TopSection()
            OrderButtonSection()
        }
        ChatListSection { moveScreen(navHostController, NavigationRoutes.NAVHOSTCHAT) }
    }
}

@Composable
private fun TopSection() {
    Row {
        Text(
            text = stringResource(id = R.string.title_activity_chat_tab),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun OrderButtonSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 25.dp, bottom = 15.dp),
        horizontalArrangement = Arrangement.End
    ) {
        FilterButton(stringResource(R.string.screen_chat_list_btn_up_to_date_order)) {}
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListSection(onClick: () -> Unit) {
    LazyColumn {
        items(5) {
            ChatListItem { onClick() }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ChatListScreenPreview() {
    RentItTheme {
        ChatListScreen(rememberNavController())
    }
}