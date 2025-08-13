package com.example.rentit.common.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 화면 기본 수평 여백을 적용하는 Modifier
fun Modifier.screenHorizontalPadding(): Modifier {
    return this.then(Modifier.padding(horizontal = 30.dp))
}

// 화면 하단에 Button이 고정으로 사용될 경우 필요한 하단 여백을 적용하는 Modifier
fun Modifier.paddingForBottomBarButton(): Modifier {
    return this.then(Modifier.padding(bottom = 30.dp))
}