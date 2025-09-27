package com.example.rentit.common.ui.component.item

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rentit.common.theme.AppRed

/**
 * 입력 필드 하단에 표시되는 에러 메세지
 */

@Composable
fun RentItInputErrorMessage(text: String) {
    Text(
        modifier = Modifier.padding(top = 10.dp, start = 12.dp, end = 12.dp),
        text = text,
        color = AppRed,
        style = MaterialTheme.typography.labelLarge
    )
}
