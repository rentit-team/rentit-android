package com.example.rentit.presentation.productdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonBorders
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.LoadableUrlImage
import com.example.rentit.common.component.formatPeriodTextWithLabel
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.formatPrice
import com.example.rentit.domain.product.model.ProductDetailModel
import com.example.rentit.presentation.productdetail.rentalhistory.RentalHistoryBottomDrawer

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productDetail: ProductDetailModel,
    requestCount: Int?,
    sheetState: SheetState,
    showBottomSheet: Boolean,
    showFullImage: Boolean,
    onBackClick: () -> Unit,
    onLikeClick: () -> Unit,
    onShareClick: () -> Unit,
    onRentalHistoryClick: () -> Unit,
    onChattingClick: () -> Unit,
    onResvRequestClick: () -> Unit,
    onFullImageDismiss: () -> Unit,
    onFullImageShow: () -> Unit,
    onBottomSheetShow: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { CommonTopAppBar(onBackClick = onBackClick) },
        bottomBar = {
            PostBottomBar(
                price = productDetail.price,
                minPeriod = productDetail.minPeriod,
                maxPeriod = productDetail.maxPeriod,
                requestCount = requestCount,
                onResvRequestClick = onResvRequestClick,
                onRentalHistoryClick = onRentalHistoryClick,
                onChattingClick = onChattingClick
            )
        },
        floatingActionButton = { UsageDetailButton(onBottomSheetShow) }
    ) { innerPadding ->
        if(productDetail != ProductDetailModel.EMPTY) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(state = rememberScrollState())
            ) {
                ImagePager(productDetail.imgUrlList, onFullImageShow)
                PostHeader(
                    productDetail.title,
                    productDetail.category,
                    productDetail.createdAt.substring(0, 10),
                    onLikeClick = onLikeClick,
                    onShareClick = onShareClick
                )
                Text(
                    modifier = Modifier
                        .screenHorizontalPadding()
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
            onDismissRequest = onFullImageDismiss,
            sheetState = sheetState
        ) {
            RentalHistoryBottomDrawer(productDetail.productId)
        }
    }

    if(showFullImage) FullImagePager(productDetail.imgUrlList, onFullImageDismiss)
}

@Composable
fun ImagePager(imgUrlList: List<String?>, onClick: () -> Unit) {
    if(imgUrlList.isEmpty()){
        Image(
            modifier = Modifier.height(290.dp),
            painter = painterResource(id = R.drawable.img_placeholder),
            contentDescription = stringResource(id = R.string.screen_product_detail_img_placeholder_description),
            contentScale = ContentScale.FillWidth
        )
    } else {
        val pagerState = rememberPagerState { imgUrlList.size }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
        ) { page ->
            LoadableUrlImage(
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
fun FullImagePager(imgUrlList: List<String?>, onClick: () -> Unit) {
    val pagerState = rememberPagerState { imgUrlList.size }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.Black.copy(alpha = 0.5f))) {
        Image(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
                .clickable { onClick() },
            painter = painterResource(id = R.drawable.ic_x),
            contentDescription = "닫기",
            colorFilter = ColorFilter.tint(Color.White)
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) { page ->
            LoadableUrlImage(
                modifier = Modifier.fillMaxHeight(0.7f),
                imgUrl = imgUrlList[page],
                defaultImageResId = R.drawable.img_placeholder,
                defaultDescResId = R.string.screen_product_detail_img_description,
                contentScale = ContentScale.FillWidth
            )
        }
    }
}


@Composable
fun PostHeader(title: String, category: List<String>, creationDate: String, onLikeClick: () -> Unit, onShareClick: () -> Unit) {
    Row(Modifier
        .fillMaxWidth()
        .screenHorizontalPadding()
        .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge)
            }
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "${category.joinToString(" · ") }  $creationDate",
                style = MaterialTheme.typography.labelMedium,
                color = Gray400
            )
        }
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
}

@Composable
fun PostBottomBar(
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
        .screenHorizontalPadding()
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
        if(requestCount != null) {
            MiniButton(false, stringResource(id = R.string.screen_product_btn_request, requestCount), onRentalHistoryClick)
        } else {
            MiniButton(false, stringResource(id = R.string.screen_product_btn_chatting), onChattingClick)
            MiniButton(true, stringResource(id = R.string.screen_product_btn_reserve), onResvRequestClick)
        }
    }
}

@Composable
fun UsageDetailButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        containerColor = Color.White,
        onClick = onClick,
    ) {
        Image(
            modifier = Modifier.padding(end = 8.dp),
            painter = painterResource(id = R.drawable.ic_calendar),
            contentDescription = stringResource(id = R.string.screen_product_btn_check_detail_of_use)
        )
        Text(text = stringResource(id = R.string.screen_product_btn_check_detail_of_use))
    }
}

@Composable
fun MiniButton(isOutlinedButton: Boolean, text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .height(38.dp)
            .padding(start = 9.dp),
        border = if(isOutlinedButton) CommonBorders.basicBorder() else null,
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
        imgUrlList = listOf("sample.jpg"),
        minPeriod = 3,
        maxPeriod = 5
    )

    RentItTheme {
        ProductDetailScreen(
            productDetail = sampleProductDetail,
            requestCount = 2,
            showBottomSheet = false,
            sheetState = rememberModalBottomSheetState(),
            showFullImage = false,
            onRentalHistoryClick = { },
            onChattingClick = { },
            onResvRequestClick = { },
            onBackClick = { },
            onFullImageDismiss = { },
            onFullImageShow = { },
            onBottomSheetShow = { },
            onLikeClick = { },
            onShareClick = { }
        )
    }
}