package com.example.rentit.common.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.formatPrice
import com.example.rentit.common.util.formatRentalPeriod

/**
* 대여 정보(상품 사진, 게시글 제목, 대여 기간, 총 금액)을 함께 표시하는 UI 컴포넌트
 */

private const val imageFraction = 0.23f
private val contentStartPadding = 15.dp
private val periodTextVerticalPadding = 8.dp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalSummary(
    modifier: Modifier = Modifier,
    thumbnailImgUrl: String? = null,
    productTitle: String,
    startDate: String,
    endDate: String,
    totalPrice: Int,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(modifier.fillMaxWidth().clickable(isClickable) { onClick() }, verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
        LoadableUrlImage(
            modifier = Modifier.fillMaxSize(imageFraction).aspectRatio(1f).clip(RoundedCornerShape(20.dp)),
            imgUrl = thumbnailImgUrl,
            defaultImageResId = R.drawable.img_thumbnail_placeholder,
        )

        Column(Modifier.padding(start = contentStartPadding)) {
            Text(
                text = productTitle,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(vertical = periodTextVerticalPadding),
                text = formatRentalPeriod(LocalContext.current, startDate, endDate),
                style = MaterialTheme.typography.labelMedium,
                color = Gray800
            )
            Text(
                text = "${stringResource(R.string.common_total)} " +
                        "${formatPrice(totalPrice)} " +
                        stringResource(R.string.common_price_unit),
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
        RentalSummary(
            productTitle = "캐논 EOS 550D",
            startDate = "2025-08-17",
            endDate = "2025-08-20",
            totalPrice = 700000,
        )
    }
}