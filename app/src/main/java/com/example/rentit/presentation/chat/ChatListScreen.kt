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
import com.example.rentit.common.util.toShortFormat
import com.example.rentit.domain.chat.model.ChatRoomSummaryModel
import java.time.OffsetDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListScreen(
    chatRoomSummaries: List<ChatRoomSummaryModel> = emptyList(),
    onItemClick: (String) -> Unit = {},
) {
    Column(Modifier.fillMaxSize().padding(top = 45.dp)) {

        ChatListTopSection()

        ChatListSection(chatRoomSummaries, onItemClick)
    }
}

@Composable
fun ChatListTopSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .screenHorizontalPadding()
            .padding(bottom = 15.dp)
    ) {

        Text(stringResource(id = R.string.title_activity_chat_tab))

        Spacer(modifier = Modifier.height(25.dp))

        FilterButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 15.dp),
            title = stringResource(R.string.screen_chat_list_btn_up_to_date_order)
        ) {}
    }
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
    val formatLastMsgTime = lastMessageTime?.toShortFormat() ?: ""

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
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.width(160.dp),
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
                    text = when (lastMessage) {
                        AutoMsgType.REQUEST_ACCEPT.code -> stringResource(R.string.auto_msg_type_request_accept_title)
                        AutoMsgType.COMPLETE_PAY.code -> stringResource(R.string.auto_msg_type_pay_complete_title)
                        else -> lastMessage
                    },
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ChatListScreenPreview() {
    RentItTheme {
        ChatListScreen()
    }
}