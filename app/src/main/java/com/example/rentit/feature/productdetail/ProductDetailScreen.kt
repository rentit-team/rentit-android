package com.example.rentit.feature.productdetail

import android.os.Build
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.rentit.R
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.component.moveScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.storage.getMyIdFromPrefs
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.Gray800
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.feature.productdetail.rentalhistory.RentalHistoryBottomDrawer
import java.text.NumberFormat

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navHostController: NavHostController, productId: Int?) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }
    var showFullImage by remember { mutableStateOf(false) }

    val productDetailViewModel: ProductDetailViewModel = hiltViewModel()
    val productDetailResult by productDetailViewModel.productDetail.collectAsStateWithLifecycle()
    val requestHistory by productDetailViewModel.requestList.collectAsStateWithLifecycle()

    val productDetail = productDetailResult?.getOrNull()?.product
    val ownerId = productDetail?.owner?.userId ?: -1

    val myId = getMyIdFromPrefs(LocalContext.current).toInt()
    val isMyProduct = ownerId > -1 && myId == ownerId

    val context = LocalContext.current

    LaunchedEffect(productId) {
        if(productId == null) {
            Toast.makeText(context, context.getString(R.string.error_common_cant_find_product), Toast.LENGTH_SHORT).show()
            navHostController.popBackStack()
        } else {
            productDetailViewModel.getProductDetail(productId)
            if(isMyProduct)
                productDetailViewModel.getProductRequestList(productId)
        }
    }

    /* productDetail 로딩 실패 시 UI 필요 */

    /*val imgUrlList = listOf(
        "https://t1.daumcdn.net/thumb/R720x0/?fname=http://t1.daumcdn.net/brunch/service/user/14Fa/image/joib7vCDm4iIP7rNJR2ojev0A20.jpg",
        "https://media.istockphoto.com/id/520700958/ko/%EC%82%AC%EC%A7%84/%EC%95%84%EB%A6%84%EB%8B%A4%EC%9A%B4-%EA%BD%83-%EB%B0%B0%EA%B2%BD%EA%B8%B0%EC%88%A0.jpg?s=612x612&w=0&k=20&c=gJx5-O9U1qXKZqKwv4KunrBae7RDNRcdse1nOdSk_0w="
        )*/

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { CommonTopAppBar(onClick = { /*TODO*/ }) },
        bottomBar = { PostBottomBar(navHostController, productDetail?.price ?: 0, isMyProduct, requestHistory.size) },
        floatingActionButton = { UsageDetailButton { showBottomSheet = true } }
    ) { innerPadding ->
        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()) {
            Column(
                modifier = Modifier
                    .verticalScroll(state = rememberScrollState())
            ) {
                ImagePager(listOf(productDetail?.thumbnailImgUrl)) { showFullImage = true; Log.d("CLICKED", "showFullImage");}
                PostHeader(productDetail?.title ?: "" , "카테고리",
                    "${productDetail?.createdAt?.substring(0, 10)}"
                )
                Text(
                    modifier = Modifier
                        .screenHorizontalPadding()
                        .fillMaxWidth(),
                    text = productDetail?.description ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray800
                )
                if(showBottomSheet) {
                    ModalBottomSheet(onDismissRequest = { showBottomSheet = false }, sheetState = sheetState) {
                        RentalHistoryBottomDrawer(productId ?: -1)
                    }
                }
            }
        }
    }
    //if(showFullImage) FullImagePager(imgUrlList) { showFullImage = false }
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
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            AsyncImage(
                modifier = Modifier
                    .height(290.dp)
                    .clickable { onClick },
                placeholder = painterResource(id = R.drawable.img_placeholder),
                error = painterResource(id = R.drawable.img_placeholder),
                model = imgUrlList[page],
                contentDescription = stringResource(id = R.string.screen_product_detail_img_description),
                contentScale = ContentScale.Crop
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
fun FullImagePager(imgUrlList: List<String>, onClick: () -> Unit) {
    val pagerState = rememberPagerState { imgUrlList.size }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color(0, 0, 0, 180))) {
        Image(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(20.dp)
                .clickable { onClick },
            painter = painterResource(id = R.drawable.ic_x),
            contentDescription = "닫기",
            colorFilter = ColorFilter.tint(Color.White),)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                modifier = Modifier
                    .height(290.dp),
                placeholder = painterResource(id = R.drawable.img_placeholder),
                model = imgUrlList[page],
                contentDescription = stringResource(id = R.string.screen_product_detail_img_description),
                contentScale = ContentScale.FillWidth
            )
        }
    }
}


@Composable
fun PostHeader(title: String, category: String, creationDate: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .screenHorizontalPadding()
            .padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = "$category · $creationDate",
                style = MaterialTheme.typography.labelMedium,
                color = Gray400
            )
        }
        Row(modifier = Modifier.height(24.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.width(24.dp),
                painter = painterResource(id = R.drawable.ic_like),
                contentDescription = stringResource(id = R.string.screen_product_like_icon_description)
            )
            Image(
                modifier = Modifier.padding(start = 12.dp),
                painter = painterResource(id = R.drawable.ic_share),
                contentDescription = stringResource(id = R.string.screen_product_share_icon_description)
            )
        }
    }
}

@Composable
fun PostBottomBar(navHostController: NavHostController, price: Int, isMyProduct: Boolean, requestCount: Int) {
    val formattedPrice = NumberFormat.getNumberInstance().format(price)
    // Shadow for bottom bar
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Gray100)
            )
        ))
    Row(modifier = Modifier
        .fillMaxWidth()
        .screenHorizontalPadding()
        .padding(vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.weight(1F),
            text = "$formattedPrice " + stringResource(id = R.string.common_price_unit),
            style = MaterialTheme.typography.titleLarge
        )
        if(isMyProduct) {
            MiniButton(false, stringResource(id = R.string.screen_product_btn_request, requestCount)) { moveScreen(navHostController, NavigationRoutes.REQUESTHISTORY) }
        } else {
            MiniButton(false, stringResource(id = R.string.screen_product_btn_chatting)) {}
            MiniButton(true, stringResource(id = R.string.screen_product_btn_reserve)) { moveScreen(navHostController, NavigationRoutes.RESVREQUEST)  }
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
fun MiniButton(isBgColorWhite: Boolean, text: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .height(38.dp)
            .padding(start = 9.dp),
        border = if(isBgColorWhite) BorderStroke(1.dp, Gray200) else null,
        contentPadding = PaddingValues(vertical = 4.dp, horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if(isBgColorWhite) Color.White else PrimaryBlue500
        ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = if(isBgColorWhite) PrimaryBlue500 else Color.White
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview() {
    RentItTheme {
        ProductDetailScreen(rememberNavController(), hiltViewModel())
    }
}