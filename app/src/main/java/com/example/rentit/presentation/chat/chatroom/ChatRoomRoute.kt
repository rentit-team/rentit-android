package com.example.rentit.presentation.chat.chatroom

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.example.rentit.R

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatroomRoute(navHostController: NavHostController, chatRoomId: String) {
    val viewModel: ChatRoomViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context: Context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val inputScrollState = rememberScrollState()
    var messageValue by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) {
        viewModel.fetchChatRoomData(chatRoomId)
        viewModel.connectWebSocket(chatRoomId)
    }

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.sideEffect.collect { sideEffect ->
                when (sideEffect) {
                    ChatRoomSideEffect.ToastChatDisconnect -> {
                        Toast.makeText(context, context.getString(R.string.toast_chat_disconnect), Toast.LENGTH_SHORT).show()
                    }
                    ChatRoomSideEffect.ToastMessageSendFailed -> {
                        Toast.makeText(context, context.getString(R.string.toast_chat_message_send_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
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
        isLoading = uiState.isLoading,
        showNetworkErrorDialog = uiState.showNetworkErrorDialog,
        showServerErrorDialog = uiState.showServerErrorDialog,
        showForbiddenChatAccessDialog = uiState.showForbiddenChatAccessDialog,
        onRetry = { viewModel.retryFetchChatRoomData(chatRoomId) },
        onForbiddenDialogDismiss = navHostController::popBackStack,
        onMessageChange = { messageValue = it },
        onMessageSend = { viewModel.sendMessage(chatRoomId, messageValue.text) },
        navigateBack = navHostController::popBackStack
    )
}
