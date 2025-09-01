package com.example.rentit.presentation.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.navigation.chatroom.navigateToChatRoom

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListRoute(navHostController: NavHostController) {
    val chatListViewModel: ChatListViewModel = hiltViewModel()
    val uiState by chatListViewModel.uiState.collectAsStateWithLifecycle()

    ChatListScreen(
        chatRoomSummaries = uiState.chatRoomSummaries,
        isLoading = uiState.isLoading,
        showNetworkErrorDialog = uiState.showNetworkErrorDialog,
        showServerErrorDialog = uiState.showServerErrorDialog,
        onRetry = chatListViewModel::retryFetchChatRoomSummaries,
        onItemClick = { navHostController.navigateToChatRoom(it) }
    )
}