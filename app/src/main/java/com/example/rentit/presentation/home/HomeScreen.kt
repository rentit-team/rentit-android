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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.FilterButton
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.component.item.ProductListItem
import com.example.rentit.common.component.dialog.BaseDialog
import com.example.rentit.common.component.layout.LoadingScreen
import com.example.rentit.data.product.model.ProductWithCategory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    sortedProducts: List<ProductWithCategory> = emptyList(),
    isLoading: Boolean = false,
    showNetworkErrorDialog: Boolean = false,
    showServerErrorDialog: Boolean = false,
    onProductClick: (Int) -> Unit = {},
    onRetry: () -> Unit = {}
) {
    Column {

        HomeTopSection()

        HomeOptionSection()

        HomeProductListSection(sortedProducts, onProductClick)
    }

    LoadingScreen(isLoading)

    if(showNetworkErrorDialog) NetworkErrorDialog(onRetry)

    if(showServerErrorDialog) ServerErrorDialog(onRetry)
}

@Composable
fun HomeTopSection(onSearchClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .screenHorizontalPadding()
            .padding(top = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(0.2f),
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = stringResource(id = R.string.screen_home_logo_img_placeholder_description)
        )
        IconButton(
            modifier = Modifier.width(18.dp),
            onClick = onSearchClick
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = stringResource(id = R.string.screen_home_search_img_placeholder_description),
            )
        }
    }
}

@Composable
fun HomeOptionSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .screenHorizontalPadding()
            .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterButton(
            modifier = Modifier.padding(end = 9.dp),
            title = stringResource(R.string.screen_home_label_btn_filter_rent_possibility),
            contentDesc = stringResource(R.string.screen_home_label_btn_filter_rent_possibility)
        )
        FilterButton(
            title = stringResource(R.string.screen_home_label_btn_filter_category),
            contentDesc = stringResource(R.string.screen_home_filter_content_description_category)
        ) {
            Image(
                modifier = Modifier.padding(start = 4.dp),
                painter = painterResource(id = R.drawable.ic_chevron_down),
                contentDescription = null
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeProductListSection(sortedProducts: List<ProductWithCategory>, onProductClick: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
    ) {
        items(sortedProducts, key = { it.productId }) { item ->
            ProductListItem(
                price = item.price,
                title = item.title,
                thumbnailImgUrl = item.thumbnailImgUrl,
                minPeriod = item.minPeriod,
                maxPeriod = item.maxPeriod,
                categories = item.categoryNames,
                status = item.status,
                createdAt = item.createdAt,
            ) { onProductClick(item.productId) }
        }
    }
}

@Composable
fun NetworkErrorDialog(onRetry: () -> Unit = {}) {
    BaseDialog(
        title = stringResource(R.string.dialog_network_error_title),
        content = stringResource(R.string.dialog_network_error_content),
        confirmBtnText = stringResource(R.string.dialog_network_error_retry_btn),
        isBackgroundClickable = false,
        onConfirmRequest = onRetry,
    )
}

@Composable
fun ServerErrorDialog(onRetry: () -> Unit = {}) {
    BaseDialog(
        title = stringResource(R.string.dialog_server_error_title),
        content = stringResource(R.string.dialog_server_error_content),
        confirmBtnText = stringResource(R.string.dialog_server_error_retry_btn),
        isBackgroundClickable = false,
        onConfirmRequest = onRetry,
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RentItTheme {
        HomeScreen()
    }
}