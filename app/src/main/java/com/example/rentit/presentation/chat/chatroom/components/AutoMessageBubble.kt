package com.example.rentit.presentation.chat.chatroom.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.enums.AutoMessageType
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AutoMessageBubble(isSender: Boolean, type: AutoMessageType, onPayClick: () -> Unit = {}) {
    val isPayButtonAvailable = !isSender
    var title = ""
    var content = ""
    var btnText = ""
    when (type) {
        AutoMessageType.REQUEST_ACCEPTED -> {
            title = stringResource(R.string.auto_msg_type_request_accept_title)
            content = stringResource(R.string.auto_msg_type_request_accept_content)
            btnText = stringResource(R.string.auto_msg_type_request_accept_btn_text)
        }
        AutoMessageType.COMPLETE_PAY -> {
            title = stringResource(R.string.auto_msg_type_pay_complete_title)
            content = stringResource(R.string.auto_msg_type_pay_complete_content)
        }
    }
    Box(
        modifier = Modifier
            .widthIn(max = LocalConfiguration.current.screenWidthDp.dp * 0.6f)
            .clip(RoundedCornerShape(20.dp))
            .background(if(isSender) PrimaryBlue500 else Color.White)
            .padding(vertical = 16.dp, horizontal = 18.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(lineBreak = LineBreak.Simple),
                color = if(isSender) Color.White else AppBlack
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(lineBreak = LineBreak.Simple),
                color = if(isSender) Color.White else AppBlack
            )
            if(type == AutoMessageType.REQUEST_ACCEPTED){
                Button(
                    onClick = onPayClick,
                    enabled = isPayButtonAvailable,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .height(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = AppBlack,
                        containerColor = Gray100,
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = btnText,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview() {
    RentItTheme {
        AutoMessageBubble(true, AutoMessageType.COMPLETE_PAY)
    }
}