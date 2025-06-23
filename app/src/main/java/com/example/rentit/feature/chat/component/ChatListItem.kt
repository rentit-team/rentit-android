package com.example.rentit.feature.chat.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rentit.R
import com.example.rentit.common.component.basicListItemTopDivider
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.data.chat.dto.ChatRoomDataDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatListItem() {
    val data = ChatRoomDataDto(
        chatroomId = "sadsadsadfas",
        productTitle = "캐논 EOS 550D",
        partnerNickname = "상대 아이디",
        lastMessage = "내일 가능할까요?",
        lastMessageTime = "2025-04-09T22:00:00"
    )
    val lastMessageTime = formatDateTime(data.lastMessageTime)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .basicListItemTopDivider()
            .background(Color.White)
            .clickable { })
    {
        Row(
            modifier = Modifier
                .screenHorizontalPadding()
                .padding(vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(74.dp)
                    .clip(RoundedCornerShape(20.dp)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data("")
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
                    text = data.lastMessage,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(dateTimeString: String): String {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val localDateTime = LocalDateTime.parse(dateTimeString, formatter)
    val dateFormatter = DateTimeFormatter.ofPattern("yy.MM.dd")
    return localDateTime.format(dateFormatter)
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ChatListItemPreview() {
    RentItTheme {
        ChatListItem()
    }
}