package com.example.rentit.presentation.chat.chatroom

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.example.rentit.navigation.pay.navigateToPay
import com.example.rentit.navigation.productdetail.navigateToProductDetail

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatroomRoute(navHostController: NavHostController, chatRoomId: String) {
    val viewModel: ChatRoomViewModel = hiltViewModel()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val context: Context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val inputScrollState = rememberScrollState()
    val messageScrollState = rememberLazyListState()
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
                    ChatRoomSideEffect.MessageSendSuccess -> {
                        messageValue = TextFieldValue("")
                    }
                    ChatRoomSideEffect.MessageReceived -> {
                        if(uiState.messages.isNotEmpty()) {
                            messageScrollState.animateScrollToItem(0)
                        }
                    }
                    is ChatRoomSideEffect.NavigateToPay -> {
                        navHostController.navigateToPay(sideEffect.productId, sideEffect.reservationId)
                    }
                    is ChatRoomSideEffect.NavigateToProductDetail -> {
                        navHostController.navigateToProductDetail(sideEffect.productId)
                    }
                    is ChatRoomSideEffect.NavigateToRentalDetail -> {
                        // TODO: 대여 상세로 이동
                    }
                    ChatRoomSideEffect.ToastPaymentInvalidStatus -> {
                        Toast.makeText(context, context.getString(R.string.toast_payment_invalid_status), Toast.LENGTH_SHORT).show()
                    }
                    ChatRoomSideEffect.ToastPaymentNotRenter -> {
                        Toast.makeText(context, context.getString(R.string.toast_payment_not_renter), Toast.LENGTH_SHORT).show()
                    }
                    ChatRoomSideEffect.ToastPaymentProductNotFound -> {
                        Toast.makeText(context, context.getString(R.string.toast_payment_product_not_found), Toast.LENGTH_SHORT).show()
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
        messageScrollState = messageScrollState,
        inputScrollState = inputScrollState,
        isLoading = uiState.isLoading,
        showNetworkErrorDialog = uiState.showNetworkErrorDialog,
        showServerErrorDialog = uiState.showServerErrorDialog,
        showForbiddenChatAccessDialog = uiState.showForbiddenChatAccessDialog,
        onRetry = { viewModel.retryFetchChatRoomData(chatRoomId) },
        onPayClick = viewModel::onPayClicked,
        onProductSectionClick = viewModel::onProductSectionClicked,
        onRentalSectionClick = viewModel::onRentalSectionClicked,
        onForbiddenDialogDismiss = navHostController::popBackStack,
        onMessageChange = { messageValue = it },
        onMessageSend = { viewModel.sendMessage(chatRoomId, messageValue.text) },
        navigateBack = navHostController::popBackStack
    )
}
