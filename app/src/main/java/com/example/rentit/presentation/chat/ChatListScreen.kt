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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
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
import com.example.rentit.common.ui.component.item.RentItFilterButton
import com.example.rentit.common.ui.component.item.RentItLoadableUrlImage
import com.example.rentit.common.ui.component.layout.RentItEmptyContentScreen
import com.example.rentit.common.ui.component.layout.RentItPullToRefreshLayout
import com.example.rentit.common.ui.extension.rentItScreenHorizontalPadding
import com.example.rentit.common.enums.AutoMessageType
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.ui.formatter.toRelativeDayFormat
import com.example.rentit.domain.chat.model.ChatRoomSummaryModel
import com.example.rentit.presentation.chat.model.ChatListFilter
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListScreen(
    chatRoomSummaries: List<ChatRoomSummaryModel> = emptyList(),
    scrollState: LazyListState = LazyListState(),
    pullToRefreshState: PullToRefreshState = PullToRefreshState(),
    isActiveChatRooms: Boolean = true,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onToggleFilter: (ChatListFilter) -> Unit = {},
    onItemClick: (String) -> Unit = {},
) {
    Column(Modifier
        .fillMaxSize()
        .padding(top = 45.dp)
        .background(Color.White)) {

        Text(
            modifier = Modifier.rentItScreenHorizontalPadding(),
            text = stringResource(id = R.string.title_activity_chat_tab)
        )

        Spacer(Modifier.height(22.dp))

        RentalHistoryFilterSection(isActiveChatRooms, onToggleFilter)

        RentItPullToRefreshLayout(
            isRefreshing = isRefreshing,
            pullToRefreshState = pullToRefreshState,
            onRefresh = onRefresh
        ) {
            ChatListSection(isActiveChatRooms, chatRoomSummaries, scrollState, onItemClick)
        }
    }
}

@Composable
fun RentalHistoryFilterSection(
    isActiveChatRooms: Boolean = true,
    onToggleFilter: (ChatListFilter) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .rentItScreenHorizontalPadding()
            .padding(bottom = 13.dp),
        horizontalArrangement = Arrangement.End,
    ) {
        RentItFilterButton(
            title = stringResource(R.string.screen_chat_list_filter_active_chat_room),
            contentDesc = stringResource(R.string.screen_chat_list_filter_active_chat_room_content_description),
            isSelected = isActiveChatRooms,
            onClick = { onToggleFilter(ChatListFilter.ACTIVE) }
        )

        Spacer(Modifier.width(10.dp))

        RentItFilterButton(
            title = stringResource(R.string.screen_chat_list_filter_empty_chat_room),
            contentDesc = stringResource(R.string.screen_chat_list_filter_empty_chat_room_content_description),
            isSelected = !isActiveChatRooms,
            onClick = { onToggleFilter(ChatListFilter.EMPTY) }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListSection(isActiveChatRooms: Boolean = true, chatRoomSummaries: List<ChatRoomSummaryModel>, scrollState: LazyListState, onItemClick: (String) -> Unit) {
    val emptyContentText = if(isActiveChatRooms) {
        stringResource(R.string.screen_chat_list_empty_active_chat_rooms)
    } else {
        stringResource(R.string.screen_chat_list_empty_empty_chat_rooms)
    }
    if(chatRoomSummaries.isEmpty()) return RentItEmptyContentScreen(text = emptyContentText)

    LazyColumn (
        modifier = Modifier.fillMaxSize().background(Gray100),
        state = scrollState
    ) {
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
    val formatLastMsgTime = lastMessageTime?.let { "· " + it.toRelativeDayFormat() } ?: ""

    val lastMsg = when (lastMessage) {
        AutoMessageType.REQUEST_ACCEPTED.code -> stringResource(R.string.auto_msg_type_request_accept_title)
        AutoMessageType.COMPLETE_PAY.code -> stringResource(R.string.auto_msg_type_pay_complete_title)
        "" -> stringResource(R.string.screen_chat_list_empty_chat_message)
        else -> lastMessage
    }

    val msgColor = if(lastMessage.isEmpty()) Gray400 else AppBlack

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(onClick = onClick)
    ){
        Row(
            modifier = Modifier
                .rentItScreenHorizontalPadding()
                .padding(vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RentItLoadableUrlImage(
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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = productTitle,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = lastMsg,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = msgColor
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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