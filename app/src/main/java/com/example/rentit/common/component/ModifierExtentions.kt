package com.example.rentit.common.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rentit.common.theme.Gray200

/**
 * 기본 둥근 회색 테두리
 */
fun Modifier.rentItBasicRoundedGrayBorder(color: Color = Gray200): Modifier {
    return this.then(Modifier.border(1.dp, color, RoundedCornerShape(20.dp)))
}

/**
 * 화면 기본 수평 여백 적용
 */
fun Modifier.rentItScreenHorizontalPadding(): Modifier {
    return this.then(Modifier.padding(horizontal = 30.dp))
}

/**
 * 화면 하단에 단일 버튼 고정 사용 시 필요한 하단 여백 적용
 */
fun Modifier.renItPaddingForBottomBarButton(): Modifier {
    return this.then(Modifier.padding(bottom = 30.dp))
}