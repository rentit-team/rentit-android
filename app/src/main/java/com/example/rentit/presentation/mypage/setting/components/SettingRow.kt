package com.example.rentit.presentation.mypage.setting.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.rentit.common.component.screenHorizontalPadding

private val rowHeight = 65.dp

/**
 * 설정 화면 List Item용 공통 Row
 *
 * 클릭, 패딩, contentDescription 처리 포함
 */

@Composable
fun SettingRow(
    label: String,
    contentDesc: String = label,
    isClickEnabled: Boolean = true,
    onClick: () -> Unit = {},
    endContent: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .semantics { contentDescription = contentDesc }
            .height(rowHeight)
            .clickable(enabled = isClickEnabled) { onClick() }
            .screenHorizontalPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        endContent?.invoke()
    }
}

