package com.example.rentit.common.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.common.theme.PrimaryBlue300
import com.example.rentit.common.theme.RentItTheme

/**
 * 공지 영역을 위한 UI 컴포넌트
 */

private val bannerPadding = 13.dp
private val bannerCornerRadius = 15.dp

@Composable
fun AnimatedNoticeBanner(
    modifier: Modifier = Modifier,
    noticeText: AnnotatedString,
    bgColor: Color = PrimaryBlue300
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = bgColor,
                    shape = RoundedCornerShape(bannerCornerRadius)
                )
                .padding(bannerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = noticeText,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
private fun Preview() {
    RentItTheme {
        AnimatedNoticeBanner(
            noticeText = buildAnnotatedString {
                withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {
                    append("렌팃")
                }
                append(" 테스트 텍스트")
            },
        )
    }
}