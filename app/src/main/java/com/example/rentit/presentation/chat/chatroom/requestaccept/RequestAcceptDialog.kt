package com.example.rentit.presentation.chat.chatroom.requestaccept

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.rentit.R
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import java.text.NumberFormat

@Composable
fun RequestAcceptDialog(productPrice: Int, onDismissRequest: () -> Unit, onAcceptRequest: () -> Unit) {
    val rentalPeriod = 4
    val numFormat = NumberFormat.getNumberInstance()
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0.8f, 0.8f, 0.8f, 0.5f))
                .screenHorizontalPadding()
                .clickable(
                    // 터치 효과 제거
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) { onDismissRequest() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(28.dp))
                    .padding(top = 24.dp, bottom = 12.dp, start = 24.dp, end = 24.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.modal_chatroom_accept_request_title),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.padding(top = 18.dp, bottom = 12.dp),
                    text = stringResource(R.string.modal_chatroom_accept_request_label_rental_period),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    modifier = Modifier.padding(bottom = 18.dp),
                    text = "25.08.17 (목) ~ 25.08.20 (일) · 4일",      // 백엔드 데이터 누락
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray800
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.modal_chatroom_accept_request_label_total_price),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "${numFormat.format(productPrice * rentalPeriod)} ${stringResource(R.string.common_price_unit_per_day)}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(R.string.modal_chatroom_accept_request_btn_cancel))
                    }
                    TextButton(
                        onClick = onAcceptRequest,
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.modal_chatroom_accept_request_btn_accept),
                            style = MaterialTheme.typography.bodyLarge,
                            color = PrimaryBlue500
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    RentItTheme {
        RequestAcceptDialog(10000, onDismissRequest = {}, onAcceptRequest = {})
    }
}