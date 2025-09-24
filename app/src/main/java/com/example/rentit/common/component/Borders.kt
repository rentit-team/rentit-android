package com.example.rentit.common.component

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rentit.common.theme.Gray200

/**
 * 기본 둥근 회색 테두리
 */
fun Modifier.basicRoundedGrayBorder(color: Color = Gray200): Modifier {
    return this.then(Modifier.border(1.dp, color, RoundedCornerShape(20.dp)))
}