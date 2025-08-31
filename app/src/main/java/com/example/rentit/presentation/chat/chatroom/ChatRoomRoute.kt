package com.example.rentit.presentation.chat.chatroom

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatroomRoute(navHostController: NavHostController, chatRoomId: String) {
    val viewModel: ChatRoomViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.fetchChatDetail(chatRoomId)
    }
    
    ChatroomScreen(
        messageText = uiState.messageText,
        partnerNickname = uiState.partnerNickname,
        messages = uiState.messages,
        productSummary = uiState.productSummary,
        rentalSummary = uiState.rentalSummary,
        inputScrollState = scrollState,
        onBackClick = {},
        isLoading = uiState.isLoading,
        onMessageChange = viewModel::updateMessageText
    ) { }
}
