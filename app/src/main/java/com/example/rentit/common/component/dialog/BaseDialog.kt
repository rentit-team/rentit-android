package com.example.rentit.common.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray300
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme

private val titleBottomPadding = 18.dp
private val contentTopPadding = 8.dp
private val contentBottomPadding = 16.dp
private val dialogBoxRadius = 28.dp

@Composable
fun BaseDialog(
    title: String,
    content: String? = null,
    closeBtnText: String? = null,
    confirmBtnText: String,
    isBackgroundClickable: Boolean = true,
    onCloseRequest: () -> Unit,
    onConfirmRequest: () -> Unit,
    customContent: @Composable ColumnScope.() -> Unit = {}
) {
    Dialog(
        onDismissRequest = onCloseRequest
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray300.copy(alpha = 0.5f))
                .screenHorizontalPadding()
                .clickable(
                    enabled = isBackgroundClickable,
                    // 터치 효과 제거
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) { onCloseRequest() },
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(dialogBoxRadius))
                    .padding(top = 24.dp, bottom = 12.dp, start = 24.dp, end = 24.dp)
                    .clickable(enabled = false) { }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = titleBottomPadding),
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )

                if(!content.isNullOrEmpty()) {
                    Text(
                        modifier = Modifier.padding(top  = contentTopPadding, bottom = contentBottomPadding),
                        text = content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gray800
                    )
                }

                customContent()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    if (!closeBtnText.isNullOrEmpty())
                        TextButton(
                            onClick = onCloseRequest,
                        ) {
                            Text(closeBtnText)
                        }

                    TextButton(
                        onClick = onConfirmRequest,
                    ) {
                        Text(
                            text = confirmBtnText,
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
        BaseDialog(
            title = "Dialog 제목",
            content = "Dialog 세부 내용",
            closeBtnText = null,
            confirmBtnText = "확인",
            onCloseRequest = {},
            onConfirmRequest = {},
        )
    }
}