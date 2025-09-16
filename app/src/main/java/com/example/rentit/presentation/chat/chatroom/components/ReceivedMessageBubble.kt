package com.example.rentit.presentation.chat.chatroom.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.LoadableUrlImage
import com.example.rentit.common.enums.AutoMessageType
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.White
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReceivedMessageBubble(msg: String, sentAt: String, senderNickname: String, onPayClick: () -> Unit = {}) {
    val msgTime = formatDateTime(sentAt)

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        LoadableUrlImage(
            modifier = Modifier.size(36.dp).clip(CircleShape),
            imgUrl = "url",
            defaultImageResId = R.drawable.img_profile_placeholder,
            defaultDescResId = R.string.content_description_for_img_profile_placeholder
        )

        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                modifier = Modifier.padding(bottom = 6.dp),
                text = senderNickname,
                style = MaterialTheme.typography.labelMedium
            )
            Row(verticalAlignment = Alignment.Bottom) {
                when (msg) {
                    AutoMessageType.REQUEST_ACCEPTED.code -> {
                        AutoMessageBubble(false, AutoMessageType.REQUEST_ACCEPTED, onPayClick = onPayClick)
                    }
                    AutoMessageType.COMPLETE_PAY.code -> {
                        AutoMessageBubble(false, AutoMessageType.COMPLETE_PAY)
                    }
                    else -> {
                        Box(
                            modifier = Modifier
                                .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.6f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(White)
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                        ) {
                            Text(
                                text = msg,
                                style = MaterialTheme.typography.bodyMedium.copy(lineBreak = LineBreak.Simple)
                            )
                        }
                    }
                }
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = msgTime,
                    style = MaterialTheme.typography.labelMedium,
                    color = Gray400
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDateTime(dateTimeString: String): String {
    val localDateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
    return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview() {
    RentItTheme {
        ReceivedMessageBubble("메세지 샘플", "2025-03-25T09:30:00Z", "홍길동")
    }
}