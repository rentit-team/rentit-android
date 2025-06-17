package com.example.rentit.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray200

@Composable
fun CommonDivider() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(Gray100))
}


fun Modifier.basicListItemTopDivider(): Modifier {
    return this.then(Modifier.drawBehind {
        drawLine(
            color = Gray200,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 1.dp.toPx()
        )
    })
}