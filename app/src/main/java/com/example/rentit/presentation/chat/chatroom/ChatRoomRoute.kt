package com.example.rentit.presentation.chat.chatroom

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatroomRoute(navHostController: NavHostController, chatRoomId: String) {
    val viewModel: ChatRoomViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val inputScrollState = rememberScrollState()
    var messageValue by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        viewModel.fetchChatDetail(chatRoomId)
        viewModel.connectWebSocket(chatRoomId, {})
    }

    LaunchedEffect(messageValue.text) {
        val isCursorAtEnd = messageValue.selection.max == messageValue.text.length
        if (isCursorAtEnd) {
            inputScrollState.animateScrollTo(inputScrollState.maxValue)
        }
    }

    DisposableEffect(Unit) {
        onDispose(onDisposeEffect = viewModel::disconnectWebSocket)
    }
    
    ChatroomScreen(
        messageText = messageValue,
        partnerNickname = uiState.partnerNickname,
        messages = uiState.messages,
        productSummary = uiState.productSummary,
        rentalSummary = uiState.rentalSummary,
        inputScrollState = inputScrollState,
        onBackClick = {},
        isLoading = uiState.isLoading,
        onMessageChange = { messageValue = it },
        onMessageSend = { viewModel.sendMessage(chatRoomId, messageValue.text) }
    )
}
