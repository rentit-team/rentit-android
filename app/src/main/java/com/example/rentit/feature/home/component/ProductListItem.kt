package com.example.rentit.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.theme.SecondaryYellow

@Composable
fun ProductListItem(onClick: () -> Unit) {
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
        }.clickable { onClick() })
    {
        Row(
            modifier = Modifier
                .screenHorizontalPadding()
                .padding(vertical = 22.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(100.dp),
                painter = painterResource(id = R.drawable.img_thumbnail_placeholder),
                contentDescription = stringResource(id = R.string.product_list_item_thumbnail_img_placeholder_description
                ))
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
                    Text(text = "게시글 제목", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "대여 상태",
                        style = MaterialTheme.typography.labelMedium,
                        color = SecondaryYellow
                    )
                }
                Text(
                    modifier = Modifier.padding(bottom = 18.dp),
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
                        text = "최소 대여 기간",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier.padding(bottom = 5.dp),
                        text = "작성일",
                        style = MaterialTheme.typography.labelMedium,
                        color = Gray400
                    )
                }

                Text(text = "가격(원)/일", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ProductListItemPreview() {
    RentItTheme {
        ProductListItem {}
    }
}