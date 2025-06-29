package com.example.rentit.feature.chat.component

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rentit.R
import com.example.rentit.common.enums.AutoMsgType
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.White
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReceivedMsgBubble(msg: String, sentAt: String, senderNickname: String) {
    val msgTime = formatDateTime(sentAt)

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        AsyncImage(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
            model = "url",
            placeholder = painterResource(id = R.drawable.img_profile_placeholder),
            error = painterResource(id = R.drawable.img_profile_placeholder),
            contentDescription = stringResource(id = R.string.content_description_for_img_profile_placeholder),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                modifier = Modifier.padding(bottom = 6.dp),
                text = senderNickname,
                style = MaterialTheme.typography.labelMedium
            )
            Row(verticalAlignment = Alignment.Bottom) {
                when (msg) {
                    AutoMsgType.REQUEST_ACCEPT.code -> {
                        AutoMsgBubble(false, AutoMsgType.REQUEST_ACCEPT, onPayClick = {})
                    }
                    AutoMsgType.COMPLETE_PAY.code -> {
                        AutoMsgBubble(false, AutoMsgType.COMPLETE_PAY)
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
fun PreviewReceivedMsgBubble() {
    RentItTheme {
        ReceivedMsgBubble("메세지 샘플", "2025-03-25T09:30:00Z", "홍길동")
    }
}