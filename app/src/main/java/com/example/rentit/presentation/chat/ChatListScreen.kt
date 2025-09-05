package com.example.rentit.presentation.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.LoadableUrlImage
import com.example.rentit.common.component.basicListItemTopDivider
import com.example.rentit.common.component.dialog.NetworkErrorDialog
import com.example.rentit.common.component.dialog.ServerErrorDialog
import com.example.rentit.common.component.layout.LoadingScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.enums.AutoMessageType
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.domain.chat.model.ChatRoomSummaryModel
import java.time.Duration
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListScreen(
    chatRoomSummaries: List<ChatRoomSummaryModel> = emptyList(),
    isLoading: Boolean = false,
    showNetworkErrorDialog: Boolean = false,
    showServerErrorDialog: Boolean = false,
    onItemClick: (String) -> Unit = {},
    navigateBack: () -> Unit = {},
    onRetry: () -> Unit = {},
) {
    Column(Modifier
        .fillMaxSize()
        .padding(top = 45.dp)
        .background(Color.White)) {

        Text(
            modifier = Modifier.screenHorizontalPadding(),
            text = stringResource(id = R.string.title_activity_chat_tab)
        )

        Spacer(Modifier.height(20.dp))

        ChatListSection(chatRoomSummaries, onItemClick)
    }

    LoadingScreen(isLoading)

    if(showNetworkErrorDialog) NetworkErrorDialog(navigateBack, onRetry)

    if(showServerErrorDialog) ServerErrorDialog(navigateBack, onRetry)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListSection(chatRoomSummaries: List<ChatRoomSummaryModel>, onItemClick: (String) -> Unit) {
    LazyColumn {
        items(chatRoomSummaries) {
            ChatListItem(
                lastMessageTime = it.lastMessageTime,
                lastMessage = it.lastMessage,
                partnerNickname = it.partnerNickname,
                productTitle = it.productTitle,
                thumbnailImgUrl = it.thumbnailImgUrl
            ) { onItemClick(it.chatRoomId) }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListItem(
    lastMessageTime: OffsetDateTime?,
    lastMessage: String,
    partnerNickname: String,
    productTitle: String,
    thumbnailImgUrl: String?,
    onClick: () -> Unit
) {
    val formatLastMsgTime = lastMessageTime?.toChatTimeString() ?: ""

    val lastMsg = when (lastMessage) {
        AutoMessageType.REQUEST_ACCEPT.code -> stringResource(R.string.auto_msg_type_request_accept_title)
        AutoMessageType.COMPLETE_PAY.code -> stringResource(R.string.auto_msg_type_pay_complete_title)
        "" -> stringResource(R.string.screen_chat_list_empty_chat_message)
        else -> lastMessage
    }

    val msgColor = if(lastMessage.isEmpty()) Gray400 else AppBlack

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .basicListItemTopDivider()
            .clickable(onClick = onClick)
    ){
        Row(
            modifier = Modifier
                .screenHorizontalPadding()
                .padding(vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LoadableUrlImage(
                modifier = Modifier.size(74.dp).clip(RoundedCornerShape(20.dp)),
                imgUrl = thumbnailImgUrl,
                defaultImageResId = R.drawable.img_thumbnail_placeholder,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        maxLines = 1,
                        text = partnerNickname,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = formatLastMsgTime,
                        style = MaterialTheme.typography.labelMedium,
                        color = Gray400
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                    text = productTitle,
                )
                Text(
                    text = lastMsg,
                    style = MaterialTheme.typography.labelMedium,
                    color = msgColor
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OffsetDateTime.toChatTimeString(): String {
    val now = LocalDate.now()
    val diffDays = Duration.between(this.toLocalDate().atStartOfDay(), now.atStartOfDay()).toDays()

    return when {
        diffDays == 0L -> {
            val formatter = DateTimeFormatter.ofPattern("· a h:mm")
            this.format(formatter)
        }
        diffDays in 1..29 -> stringResource(R.string.screen_chat_list_time_days_ago, diffDays)
        diffDays > 29 -> stringResource(R.string.screen_chat_list_time_months_ago, diffDays / 30)
        else -> ""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ChatListScreenPreview() {
    RentItTheme {
        ChatListScreen(listOf(
            ChatRoomSummaryModel(
                chatRoomId = "1",
                productTitle = "자전거 대여",
                thumbnailImgUrl = "https://example.com/bike.jpg",
                partnerNickname = "홍길동",
                lastMessage = "오늘 자전거 반납 가능해요?",
                lastMessageTime = OffsetDateTime.now().minusHours(2)
            ),
            ChatRoomSummaryModel(
                chatRoomId = "3",
                productTitle = "노트북 대여",
                thumbnailImgUrl = null,
                partnerNickname = "이영희",
                lastMessage = "감사합니다!",
                lastMessageTime = OffsetDateTime.now().minusDays(30)
            ),
            ChatRoomSummaryModel(
                chatRoomId = "3",
                productTitle = "노트북 대여",
                thumbnailImgUrl = null,
                partnerNickname = "이영희",
                lastMessage = "",
                lastMessageTime = null
            )
        ))
    }
}