package com.example.rentit.feature.chat

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rentit.R
import com.example.rentit.common.component.BookingStatus
import com.example.rentit.common.component.CommonBorders
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.component.moveScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.exception.chat.ForbiddenChatAccessException
import com.example.rentit.common.exception.ServerException
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.chat.dto.StatusHistoryDto
import com.example.rentit.data.product.dto.ProductDto
import com.example.rentit.feature.chat.component.ReceivedMsgBubble
import com.example.rentit.feature.chat.component.SentMsgBubble
import com.example.rentit.feature.chat.model.ChatMessageUiModel
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatroomScreen(navHostController: NavHostController, productId: Int?, chatRoomId: String?) {
    val chatViewModel: ChatViewModel = hiltViewModel()
    val productDetail by chatViewModel.productDetail.collectAsStateWithLifecycle()
    val chatDetail by chatViewModel.chatDetail.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val senderNickname by chatViewModel.senderNickname.collectAsStateWithLifecycle()
    val initialMessages by chatViewModel.initialMessages.collectAsStateWithLifecycle()
    val realTimeMessages by chatViewModel.realTimeMessages.collectAsStateWithLifecycle()

    var textFieldValue by remember { mutableStateOf(TextFieldValue("")) }
    val inputScrollState = rememberScrollState()
    var showAcceptDialog by remember { mutableStateOf(false) }

    val isCursorAtEnd = textFieldValue.selection.max == textFieldValue.text.length

    LaunchedEffect(Unit) {
        var errorMsg = context.getString(R.string.error_chat_unknown)
        if (chatRoomId != null && productId != null) {
            chatViewModel.connectWebSocket(chatRoomId) {
                Toast.makeText(context, context.getString(R.string.toast_chatroom_entered), Toast.LENGTH_SHORT).show()
            }
            chatViewModel.getChatDetail(chatRoomId){
                when(it){
                    is ForbiddenChatAccessException -> errorMsg = context.getString(R.string.error_chat_access)
                    is ServerException -> errorMsg = context.getString(R.string.error_common_server)
                    else -> Unit
                }
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                moveScreen(navHostController, NavigationRoutes.MAIN)
            }
            chatViewModel.getProductDetail(productId){
                Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
                moveScreen(navHostController, NavigationRoutes.MAIN)
            }
        } else {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
            moveScreen(navHostController, NavigationRoutes.MAIN)
        }
    }

    LaunchedEffect(textFieldValue.text) {
        if (isCursorAtEnd) {
            inputScrollState.animateScrollTo(inputScrollState.maxValue)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            chatViewModel.disconnectWebSocket()
        }
    }

    Column(Modifier.background(Color.White)) {
        CommonTopAppBar(onClick = { /*TODO*/ })
        // 상단 예약 관련 정보
        productDetail?.let { ProductInfo(it.product) } ?: Column(Modifier
            .fillMaxWidth()
            .height(100.dp)) {}
        chatDetail?.let { detail ->
            detail.chatRoom.statusHistory.lastOrNull()?.let { statusInfo ->
                RequestInfo(statusInfo)
                if (BookingStatus.isPending(statusInfo.status)) {
                    BookingActions(onAcceptAction = { showAcceptDialog = true })
                }
            }
            ChatMsgList(
                Modifier.weight(1F),
                senderNickname ?: stringResource(R.string.screen_chatroom_nickname_unknown),
                (realTimeMessages + initialMessages),
            )
        } ?: Column(Modifier.weight(1F)){}
        BottomInputBar(
            textFieldValue = textFieldValue,
            scrollState = inputScrollState,
            onValueChange = { textFieldValue = it },
            onSend = {
                if (chatRoomId != null && textFieldValue.text.isNotEmpty()) {
                    chatViewModel.sendMessage(chatRoomId, textFieldValue.text)
                    textFieldValue = TextFieldValue("")
                }
            }
        )
    }
    if (showAcceptDialog) {
        RequestAcceptDialog(
            onDismissRequest = { showAcceptDialog = false },
            onAcceptRequest = { moveScreen(navHostController, NavigationRoutes.ACCEPTCONFIRM) }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ProductInfo(productInfo: ProductDto) {
    val lastMessageTime = formatDateTime(productInfo.createdAt)
    val numFormatter = NumberFormat.getNumberInstance()
    val period = productInfo.period
    Row(
        modifier = Modifier
            .screenHorizontalPadding()
            .padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(15.dp)),
            model = ImageRequest.Builder(LocalContext.current)
                .data(productInfo.thumbnailImgUrl)
                .error(R.drawable.img_thumbnail_placeholder)
                .placeholder(R.drawable.img_thumbnail_placeholder)
                .fallback(R.drawable.img_thumbnail_placeholder)
                .build(),
            contentDescription = stringResource(id = R.string.common_list_item_thumbnail_img_placeholder_description),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    modifier = Modifier.width(160.dp),
                    maxLines = 1,
                    text = productInfo.title,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = if (period != null) {
                        if (period.min != null && period.max != null) stringResource(
                            R.string.product_list_item_period_text_more_and_less_than_day,
                            period.min.toInt(),
                            period.max.toInt()
                        )
                        else if (period.min != null) stringResource(
                            R.string.product_list_item_period_text_more_than_day,
                            period.min.toInt()
                        )
                        else if (period.max != null) stringResource(
                            R.string.product_list_item_period_text_less_than_day,
                            period.max.toInt()
                        )
                        else stringResource(R.string.product_list_item_period_text_more_than_zero)
                    } else {
                        stringResource(R.string.product_list_item_period_text_more_than_zero)
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = PrimaryBlue500
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = numFormatter.format(productInfo.price) + stringResource(R.string.common_price_unit),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Gray400
                )
                Text(
                    text = lastMessageTime,
                    style = MaterialTheme.typography.labelMedium,
                    color = Gray400
                )
            }
        }
    }
}

// 요청 정보
@Composable
private fun RequestInfo(statusInfo: StatusHistoryDto) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .screenHorizontalPadding()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(
                modifier = Modifier.padding(end = 6.dp),
                text = stringResource(R.string.screen_chatroom_request_info_label),
                style = MaterialTheme.typography.labelLarge,
            )
            BookingStatus.fromLabel(statusInfo.status)?.let {
                Text(
                    text = it.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = it.color
                )
            }
        }
        Text(
            text = "25.08.17 (목) ~ 25.08.20 (일) · 4일",
            style = MaterialTheme.typography.labelMedium,
            color = Gray800
        )
    }
}

// 거절하기 / 수락하기 버튼
@Composable
private fun BookingActions(onAcceptAction: () -> Unit = {}, onRejectAction: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .screenHorizontalPadding()
            .padding(bottom = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        ActionButton(
            modifier = Modifier.weight(1F),
            text = stringResource(R.string.screen_chatroom_request_action_btn_reject),
            textColor = Gray400,
        ) { onRejectAction() }
        ActionButton(
            modifier = Modifier.weight(1F),
            text = stringResource(R.string.screen_chatroom_request_action_btn_accept),
            textColor = PrimaryBlue500,
        ) { onAcceptAction() }
    }
}

// 채팅 메세지 리스트
@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ChatMsgList(
    modifier: Modifier,
    senderNickname: String,
    msgList: List<ChatMessageUiModel>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(Gray100)
            .padding(horizontal = 15.dp),
        reverseLayout = true
    ) {
        items(msgList) { msg ->
            if (msg.isMine) {
                SentMsgBubble(msg.message, msg.sentAt)
            } else {
                ReceivedMsgBubble(msg.message, msg.sentAt, senderNickname)
            }
        }
    }
}

@Composable
private fun BottomInputBar(
    textFieldValue: TextFieldValue,
    scrollState: ScrollState,
    onValueChange: (TextFieldValue) -> Unit = {},
    onSend: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .screenHorizontalPadding()
            .padding(top = 14.dp, bottom = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BasicTextField(
            value = textFieldValue,
            onValueChange = onValueChange,
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .heightIn(min = 34.dp, max = 110.dp)
                .verticalScroll(scrollState)
                .weight(1F),
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            ),
            maxLines = Int.MAX_VALUE,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .background(Gray100)
                        .padding(20.dp, 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (textFieldValue.text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.screen_chatroom_input_placeholder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Gray400
                        )
                    }
                    innerTextField()
                }
            }
        )
        IconButton(
            modifier = Modifier
                .weight(0.1F), onClick = { onSend() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_send),
                tint = Gray800,
                contentDescription = stringResource(
                    id = R.string.content_description_for_ic_send
                )
            )

        }
    }
}

@Composable
private fun ActionButton(modifier: Modifier, text: String, textColor: Color, onClick: () -> Unit) {
    OutlinedButton(
        modifier = modifier.height(34.dp),
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        border = CommonBorders.basicBorder(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateTime(dateTimeString: String): String {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val localDateTime = LocalDateTime.parse(dateTimeString, formatter)
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    return localDateTime.format(dateFormatter)
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewChatroomScreen() {
    RentItTheme {
        ChatroomScreen(rememberNavController(), -1, "")
    }
}