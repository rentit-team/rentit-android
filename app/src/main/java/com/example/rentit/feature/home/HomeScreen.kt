package com.example.rentit.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonLabelButton
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.feature.home.component.ProductListItem

@Composable
fun HomeScreen() {
    RentItTheme {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .screenHorizontalPadding()
                    .padding(top = 30.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.width(70.dp),
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = stringResource(id = R.string.screen_home_logo_img_placeholder_description)
                )
                IconButton(
                    modifier = Modifier.width(18.dp),
                    content = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = stringResource(id = R.string.screen_home_search_img_placeholder_description),
                    )},
                    onClick = {},
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .screenHorizontalPadding()
                    .padding(vertical = 13.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CommonLabelButton(onClick = { /*TODO*/ }, text = stringResource(id = R.string.screen_home_label_btn_filter_rent_possibility))
                Spacer(modifier = Modifier.width(9.dp))
                CategoryFilterButton()
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
            ) {
                this.items(10) {
                    ProductListItem()
                }
            }
        }
    }
}

@Composable
fun CategoryFilterButton() {
    OutlinedButton(
        modifier = Modifier.height(26.dp),
        onClick = { },
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Gray200),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 4.dp),
                text = stringResource(id = R.string.screen_home_label_btn_filter_category),
                style = MaterialTheme.typography.labelMedium)
            Image(
                painter = painterResource(id = R.drawable.ic_chevron_down),
                contentDescription = stringResource(id = R.string.screen_home_label_btn_filter_category)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}