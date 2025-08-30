package com.example.rentit.presentation.chat

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListRoute(navHostController: NavHostController) {
    val chatListViewModel: ChatListViewModel = hiltViewModel()
    val chatRoomSummaries by chatListViewModel.chatList.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // 최초 한 번만 호출
    LaunchedEffect(Unit) {
        chatListViewModel.getChatList {
            Toast.makeText(context,context.getString(R.string.error_chat_list_load),Toast.LENGTH_SHORT).show()
        }
    }

    ChatListScreen(
        chatRoomSummaries,
        onItemClick = { }
    )
}