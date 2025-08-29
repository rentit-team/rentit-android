package com.example.rentit.presentation.chat

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rentit.R
import com.example.rentit.common.component.FilterButton
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.navigation.chatroom.navigateToChatRoom
import com.example.rentit.presentation.chat.components.ChatListItem

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListScreen(navHostController: NavHostController) {
    val chatListViewModel: ChatListViewModel = hiltViewModel()
    val chatList by chatListViewModel.chatList.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // 최초 한 번만 호출
    LaunchedEffect(Unit) {
        chatListViewModel.getChatList {
            Toast.makeText(
                context,
                context.getString(R.string.error_chat_list_load),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(top = 45.dp)
    ) {
        Column(
            modifier = Modifier
                .screenHorizontalPadding()
        ) {
            TopSection()
            OrderButtonSection()
        }
        LazyColumn {
            items(chatList) {
                ChatListItem(it) {
                    navHostController.navigateToChatRoom(it.productId, it.reservationId, it.chatRoomId)
                }
            }
        }
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
        FilterButton(title = stringResource(R.string.screen_chat_list_btn_up_to_date_order)) {}
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    RentItTheme {
        ChatListScreen(rememberNavController())
    }
}