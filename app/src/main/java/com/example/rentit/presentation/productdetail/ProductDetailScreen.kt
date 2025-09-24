package com.example.rentit.presentation.productdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.layout.RentItTopAppBar
import com.example.rentit.common.component.item.RentItExtendedFAB
import com.example.rentit.common.component.item.RentItLoadableUrlImage
import com.example.rentit.presentation.productdetail.dialog.FullImagePagerDialog
import com.example.rentit.common.component.formatter.formatPeriodTextWithLabel
import com.example.rentit.common.component.rentItScreenHorizontalPadding
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.formatPrice
import com.example.rentit.domain.product.model.ProductDetailModel
import com.example.rentit.presentation.productdetail.drawer.AvailableDateDrawer
import com.example.rentit.presentation.productdetail.drawer.MenuDrawer

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productDetail: ProductDetailModel,
    reservedDateList: List<String>,
    requestCount: Int?,
    bottomSheetState: SheetState,
    menuDrawerState: SheetState,
    imagePagerState: PagerState,
    fullImagePagerState: PagerState,
    isUserOwner: Boolean,
    showBottomSheet: Boolean,
    showMenuDrawer: Boolean,
    showFullImage: Boolean,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit,
    onRentalHistoryClick: () -> Unit,
    onChattingClick: () -> Unit,
    onResvRequestClick: () -> Unit,
    onFullImageDismiss: () -> Unit,
    onFullImageShow: () -> Unit,
    onBottomSheetShow: () -> Unit,
    onBottomSheetDismiss: () -> Unit,
    onMenuDrawerShow: () -> Unit,
    onMenuDrawerDismiss: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            RentItTopAppBar(
                showMenu = isUserOwner,
                onBackClick = onBackClick,
                onMenuClick = onMenuDrawerShow
            )
        },
        bottomBar = {
            PostBottomBar(
                isUserOwner = isUserOwner,
                price = productDetail.price,
                minPeriod = productDetail.minPeriod,
                maxPeriod = productDetail.maxPeriod,
                requestCount = requestCount,
                onResvRequestClick = onResvRequestClick,
                onRentalHistoryClick = onRentalHistoryClick,
                onChattingClick = onChattingClick
            )
        },
        floatingActionButton = {
            RentItExtendedFAB(
                iconRes = R.drawable.ic_calendar,
                textRes = R.string.screen_product_btn_check_detail_of_use,
                onClick = onBottomSheetShow
            )
        }
    ) { innerPadding ->
        if(productDetail != ProductDetailModel.EMPTY) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(state = rememberScrollState())
            ) {
                ImagePager(imagePagerState, productDetail.imageUrls, onFullImageShow)
                PostHeader(
                    productDetail.title,
                    productDetail.category,
                    productDetail.createdAt.substring(0, 10),
                    onLikeClick = onLikeClick,
                    onShareClick = onShareClick
                )
                Text(
                    modifier = Modifier
                        .rentItScreenHorizontalPadding()
                        .fillMaxSize(),
                    text = productDetail.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray800
                )
            }
        }
    }

    if(showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = onBottomSheetDismiss,
            sheetState = bottomSheetState
        ) {
            AvailableDateDrawer(reservedDateList)
        }
    }

    if(showMenuDrawer) {
        ModalBottomSheet(
            onDismissRequest = onMenuDrawerDismiss,
            sheetState = menuDrawerState
        ) {
            MenuDrawer(onEditClick, onDeleteClick)
        }
    }

    if(showFullImage) FullImagePagerDialog(fullImagePagerState, productDetail.imageUrls, onFullImageDismiss)
}

@Composable
fun ImagePager(pagerState: PagerState, imgUrlList: List<String?>, onClick: () -> Unit) {
    if(imgUrlList.isEmpty()){
        Image(
            modifier = Modifier.height(290.dp),
            painter = painterResource(id = R.drawable.img_placeholder),
            contentDescription = stringResource(id = R.string.screen_product_detail_img_placeholder_description),
            contentScale = ContentScale.FillWidth
        )
    } else {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
        ) { page ->
            RentItLoadableUrlImage(
                modifier = Modifier.height(290.dp),
                imgUrl = imgUrlList[page],
                defaultImageResId = R.drawable.img_placeholder,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 13.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if(pagerState.currentPage == iteration) Gray400 else Gray200
                Box(
                    Modifier
                        .padding(3.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(5.dp)
                )
            }
        }
    }
}



@Composable
fun PostHeader(title: String, categories: List<String>, creationDate: String, onLikeClick: () -> Unit, onShareClick: () -> Unit) {
    Row(Modifier
        .fillMaxWidth()
        .rentItScreenHorizontalPadding()
        .padding(top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f, false),
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Row(modifier = Modifier.offset(x = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onLikeClick) {
                Image(
                    painter = painterResource(id = R.drawable.ic_like),
                    contentDescription = stringResource(id = R.string.screen_product_like_icon_description)
                )
            }
            IconButton(onShareClick) {
                Image(
                    painter = painterResource(id = R.drawable.ic_share),
                    contentDescription = stringResource(id = R.string.screen_product_share_icon_description)
                )
            }
        }
    }
    Row(
        modifier = Modifier.fillMaxWidth().rentItScreenHorizontalPadding().padding(bottom = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f, false),
            text = if(categories.isNotEmpty()) categories.joinToString(" · ") else stringResource(R.string.common_empty_category),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelMedium,
            color = Gray400
        )

        Text(
            text = creationDate,
            style = MaterialTheme.typography.labelMedium,
            color = Gray400
        )
    }
}

@Composable
fun PostBottomBar(
    isUserOwner: Boolean,
    price: Int,
    minPeriod: Int?,
    maxPeriod: Int?,
    requestCount: Int?,
    onRentalHistoryClick: () -> Unit,
    onChattingClick: () -> Unit,
    onResvRequestClick: () -> Unit,
) {
    val formattedPrice = formatPrice(price)
    // Shadow for bottom bar
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .background(
            brush = Brush.verticalGradient(colors = listOf(Color.Transparent, Gray100))
        )
    )
    Row(modifier = Modifier
        .fillMaxWidth()
        .rentItScreenHorizontalPadding()
        .padding(vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Column(
            modifier = Modifier.weight(1F),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = formatPeriodTextWithLabel(minPeriod, maxPeriod),
                style = MaterialTheme.typography.labelLarge,
                color = PrimaryBlue500
            )
            Text(
                text = "$formattedPrice " + stringResource(id = R.string.common_price_unit_per_day),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        if(isUserOwner) {
            val count = requestCount ?: 0
            MiniButton(false, stringResource(id = R.string.screen_product_btn_request, count), onRentalHistoryClick)
        } else {
            MiniButton(false, stringResource(id = R.string.screen_product_btn_chatting), onChattingClick)
            MiniButton(true, stringResource(id = R.string.screen_product_btn_reserve), onResvRequestClick)
        }
    }
}

@Composable
fun MiniButton(isOutlinedButton: Boolean, text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .height(38.dp)
            .padding(start = 9.dp),
        border =  BorderStroke(1.dp, Gray200),
        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isOutlinedButton) Color.White else PrimaryBlue500
        ),
    ) {
        Text(
            text = text,
            color = if(isOutlinedButton) PrimaryBlue500 else Color.White
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview() {

    val sampleProductDetail = ProductDetailModel(
        productId = 101,
        price = 50_000,
        title = "Mountain Bike",
        category = listOf("Sports"),
        content = "A sturdy mountain bike suitable for off-road trails.",
        createdAt = "2025-09-01T10:00:00",
        imageUrls = listOf("sample.jpg"),
        minPeriod = 3,
        maxPeriod = 5
    )

    RentItTheme {
        ProductDetailScreen(
            productDetail = sampleProductDetail,
            reservedDateList = emptyList(),
            requestCount = 2,
            showBottomSheet = false,
            menuDrawerState = rememberModalBottomSheetState(),
            imagePagerState = rememberPagerState { 0 },
            fullImagePagerState = rememberPagerState { 0 },
            isUserOwner = false,
            showFullImage = false,
            onRentalHistoryClick = { },
            onChattingClick = { },
            onResvRequestClick = { },
            onBackClick = { },
            onFullImageDismiss = { },
            onFullImageShow = { },
            onBottomSheetDismiss = { },
            onBottomSheetShow = { },
            onLikeClick = { },
            onShareClick = { },
            bottomSheetState = rememberModalBottomSheetState(),
            showMenuDrawer = true,
            onEditClick = {},
            onDeleteClick = {},
            onMenuDrawerShow = {},
            onMenuDrawerDismiss = {}
        )
    }
}