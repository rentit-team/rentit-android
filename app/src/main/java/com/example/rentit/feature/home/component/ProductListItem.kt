package com.example.rentit.feature.home.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rentit.R
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.SecondaryYellow
import com.example.rentit.data.product.dto.ProductDto
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProductListItem(productInfo: ProductDto, isMyProduct: Boolean = false, onClick: () -> Unit) {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val localDateTime = LocalDateTime.parse(productInfo.createdAt, formatter)
    val period = productInfo.period

    val categoryText = productInfo?.categories?.joinToString("·") ?: ""

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = Gray200,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
            .background(Color.White)
            .clickable { onClick() })
    {
        Row(
            modifier = Modifier
                .screenHorizontalPadding()
                .padding(vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier.size(100.dp).clip(RoundedCornerShape(20.dp)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(productInfo.thumbnailImgUrl)
                    .error(R.drawable.img_thumbnail_placeholder)
                    .placeholder(R.drawable.img_thumbnail_placeholder)
                    .fallback(R.drawable.img_thumbnail_placeholder)
                    .build(),
                contentDescription = stringResource(id = R.string.product_list_item_thumbnail_img_placeholder_description),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = productInfo.title, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = productInfo.status,
                        style = MaterialTheme.typography.labelMedium,
                        color = SecondaryYellow
                    )
                }
                Text(
                    modifier = Modifier.padding(top = 4.dp, bottom = 18.dp),
                    text = "카테고리",
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
                        text = if(period != null) {
                                if(period.min != null && period.max != null) "${productInfo.period.min}일 ~ ${productInfo.period.max}일"
                                    else if(period.min != null) "${productInfo.period.min}일 이상"
                                    else if(period.max != null) "${productInfo.period.max}일 이하"
                                    else "0일 이상"
                        } else {
                            "0일 이상"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 5.dp),
                        text = "${localDateTime?.monthValue}.${localDateTime?.dayOfMonth}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Gray400
                    )
                }
                val numFormatter = NumberFormat.getNumberInstance()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = numFormatter.format(productInfo.price) + "원/일",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    if(isMyProduct){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                modifier = Modifier.padding(end = 6.dp),
                                text = "요청 2건",
                                style = MaterialTheme.typography.labelLarge,
                                color = PrimaryBlue500
                            )
                            Icon(
                                modifier = Modifier.height(10.dp),
                                painter = painterResource(id = R.drawable.ic_chevron_right),
                                contentDescription = "",
                                tint = PrimaryBlue500
                            )
                        }
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ProductListItemPreview() {
    RentItTheme {
        //ProductListItem {}
    }
}