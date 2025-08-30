package com.example.rentit.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.example.rentit.common.theme.Gray100

@Composable
fun CommonDivider(modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(Gray100))
}


fun Modifier.basicListItemTopDivider(): Modifier {
    return this.then(Modifier.drawBehind {
        val y = 0.5.dp.toPx()
        drawLine(
            color = Gray100,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1.dp.toPx()
        )
    })
}