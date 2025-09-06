package com.example.rentit.common.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 화면 기본 수평 여백 적용
  */
fun Modifier.screenHorizontalPadding(): Modifier {
    return this.then(Modifier.padding(horizontal = 30.dp))
}

/**
 * 화면 하단에 단일 버튼 고정 사용 시 필요한 하단 여백 적용
 */
fun Modifier.paddingForBottomBarButton(): Modifier {
    return this.then(Modifier.padding(bottom = 30.dp))
}