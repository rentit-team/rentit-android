package com.example.rentit.presentation.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.rentit.common.component.FilterButton
import com.example.rentit.common.component.LoadableUrlImage
import com.example.rentit.common.component.basicListItemTopDivider
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.enums.AutoMsgType
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.chat.dto.ChatRoomSummaryDto
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListScreen(
    chatRoomSummaries: List<ChatRoomSummaryDto> = emptyList(),
    onItemClick: (Int) -> Unit = {},
) {
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
            items(chatRoomSummaries) {
                ChatListItem(it) { onItemClick(0) }
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
@Composable
fun ChatListItem(data: ChatRoomSummaryDto, onClick: () -> Unit) {
    val lastMessageTime = if(data.lastMessageTime != null) formatDateTime(data.lastMessageTime) else ""

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .basicListItemTopDivider()
            .background(Color.White)
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
                imgUrl = "url",
                defaultImageResId = R.drawable.img_thumbnail_placeholder,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.width(160.dp),
                        maxLines = 1,
                        text = data.partnerNickname,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = lastMessageTime,
                        style = MaterialTheme.typography.labelMedium,
                        color = Gray400
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                    text = data.productTitle,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = when (data.lastMessage) {
                        AutoMsgType.REQUEST_ACCEPT.code -> stringResource(R.string.auto_msg_type_request_accept_title)
                        AutoMsgType.COMPLETE_PAY.code -> stringResource(R.string.auto_msg_type_pay_complete_title)
                        else -> data.lastMessage
                    },
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

// 시간대(Offset)을 포함한 ISO 8601 형식의 OffsetDateTime 포맷팅
@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateTime(dateTimeString: String): String {
    val offsetDateTime = OffsetDateTime.parse(dateTimeString)
    val localDateTime = offsetDateTime.toLocalDateTime()
    return localDateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd"))
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ChatListScreenPreview() {
    RentItTheme {
        ChatListScreen()
    }
}