package com.example.rentit.presentation.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rentit.R
import com.example.rentit.common.component.FilterButton
import com.example.rentit.common.navigation.NavigationRoutes
import com.example.rentit.common.navigation.moveScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.component.ProductListItem

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(navHostController: NavHostController, modifier: Modifier = Modifier) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val productListResult by homeViewModel.productList.collectAsState()

    LaunchedEffect(Unit) {
        homeViewModel.getProductList()
    }

    val productList = productListResult?.getOrNull()?.products ?: emptyList()

    Column(modifier = modifier) {
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
            FilterButton(stringResource(id = R.string.screen_home_label_btn_filter_rent_possibility), Modifier.padding(end = 9.dp))
            FilterButton(stringResource(id = R.string.screen_home_label_btn_filter_category)) {
                Image(
                    modifier = Modifier.padding(start = 4.dp),
                    painter = painterResource(id = R.drawable.ic_chevron_down),
                    contentDescription = stringResource(id = R.string.screen_home_label_btn_filter_category)
                )
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(productList) {
                ProductListItem(it) { moveScreen(navHostController, NavigationRoutes.PRODUCT_DETAIL_NAV_HOST + "/${it.productId}", saveStateEnabled = true, restoreStateEnabled = true) }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RentItTheme {
        HomeScreen(rememberNavController())
    }
}