package com.example.rentit.common.component.item

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.LoadableUrlImage
import com.example.rentit.common.component.basicListItemTopDivider
import com.example.rentit.common.component.formatPeriodText
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.enums.ProductStatus
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.formatPrice
import com.example.rentit.common.util.toRelativeTimeFormat
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductListItem(
    title: String = "",
    price: Int = 0,
    thumbnailImgUrl: String? = null,
    minPeriod: Int? = null ,
    maxPeriod: Int? = null,
    categories: List<String> = emptyList(),
    status: ProductStatus,
    createdAt: String,
    onClick: () -> Unit = {}
) {
    val categoryText =
        if (categories.isNotEmpty()) {
            categories.joinToString(" · ")
        } else {
            stringResource(R.string.product_list_item_text_empty_category)
        }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .basicListItemTopDivider()
            .background(Color.White)
            .clickable { onClick() })
    {
        Row(
            modifier = Modifier
                .screenHorizontalPadding()
                .padding(vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LoadableUrlImage(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(20.dp)),
                imgUrl = thumbnailImgUrl,
                defaultImageResId = R.drawable.img_thumbnail_placeholder,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        text = title,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = status.label ?: "",
                        style = MaterialTheme.typography.labelMedium,
                        color = status.color
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 4.dp, bottom = 18.dp),
                    text = categoryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.labelMedium,
                    color = Gray400
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 5.dp),
                        text = formatPeriodText(minPeriod, maxPeriod),
                        style = MaterialTheme.typography.labelLarge,
                        color = PrimaryBlue500
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 5.dp),
                        text = LocalDateTime.parse(createdAt).toRelativeTimeFormat(),
                        style = MaterialTheme.typography.labelMedium,
                        color = Gray400
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatPrice(price) + stringResource(R.string.common_price_unit_per_day),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ProductListItemPreview() {
    RentItTheme {
        ProductListItem(
            status = ProductStatus.AVAILABLE,
            createdAt = "2025-03-22T12:00:00",
        )
    }
}