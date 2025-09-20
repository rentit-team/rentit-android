package com.example.rentit.common.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.rentit.R
import com.example.rentit.common.component.LoadableUrlImage
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.RentItTheme

@Composable
fun FullImagePagerDialog(
    pagerState: PagerState,
    imageUrls: List<String?>,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBlack.copy(alpha = 0.8f))
                .clickable(
                    // 터치 효과 제거
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.CenterVertically
            ) { page ->
                LoadableUrlImage(
                    modifier = Modifier.fillMaxHeight(0.7f),
                    imgUrl = imageUrls[page],
                    defaultImageResId = R.drawable.img_placeholder,
                    defaultDescResId = R.string.screen_product_detail_img_description,
                    contentScale = ContentScale.FillWidth
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FullImagePagerDialogPreview() {
    val sampleImageUrls =
        listOf(
            "https://www.econovill.com/news/photo/201812/353683_236958_5647.jpg",
            "https://flexible.img.hani.co.kr/flexible/normal/590/590/imgdb/resize/2007/1227/68227042_20071227.jpg"
            )
    RentItTheme {
        FullImagePagerDialog(
            pagerState = rememberPagerState(1) { sampleImageUrls.size },
            imageUrls = sampleImageUrls,
            onDismiss = {},
        )
    }
}