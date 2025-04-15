package com.example.rentit.common.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 화면 기본 수평 여백을 적용하는 Modifier
fun Modifier.screenHorizontalPadding(): Modifier {
    return this.then(Modifier.padding(horizontal = 25.dp))
}