package com.example.rentit.presentation.rentaldetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.ui.extension.rentItScreenHorizontalPadding
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.RentItTheme

/**
 * 라벨과 구분선이 포함된 섹션 레이아웃
 */

private val sectionTopPadding = 24.dp
private val labelBottomPadding = 20.dp
private val dividerTopPadding = 20.dp
private val dividerHeight = 8.dp

@Composable
fun TitledContainer(
    modifier: Modifier = Modifier,
    labelText: AnnotatedString,
    labelColor: Color = AppBlack,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier.padding(top = sectionTopPadding)) {
        Column(Modifier.rentItScreenHorizontalPadding()) {
            Text(
                modifier = Modifier.padding(bottom = labelBottomPadding),
                text = labelText,
                style = MaterialTheme.typography.bodyLarge,
                color = labelColor
            )
            content()
        }
        Box(
            modifier = Modifier
                .padding(top = dividerTopPadding)
                .fillMaxWidth()
                .height(dividerHeight)
                .background(Gray100)
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun Preview() {
    RentItTheme {
        TitledContainer(
            labelText = AnnotatedString("대여 요금 상세")
        ) {}
    }
}