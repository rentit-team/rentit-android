package com.example.rentit.presentation.mypage

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.rentit.R
import com.example.rentit.common.component.CommonDivider
import com.example.rentit.common.component.NavigationRoutes
import com.example.rentit.common.component.basicRoundedGrayBorder
import com.example.rentit.common.component.moveScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.AppRed
import com.example.rentit.common.theme.Gray200
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.pretendardFamily
import com.example.rentit.data.product.dto.ProductDto
import com.example.rentit.data.user.dto.ReservationDto
import com.example.rentit.common.component.ProductListItem
import com.example.rentit.presentation.mypage.components.MyRentalHistoryListItem

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyPageScreen(navHostController: NavHostController) {
    val myPageViewModel: MyPageViewModel = hiltViewModel()

    var isFirstTabSelected by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        myPageViewModel.getMyProductList()
        myPageViewModel.getMyRentalList()
    }

    val myProductList by myPageViewModel.myProductList.collectAsStateWithLifecycle()
    val myRentalList by myPageViewModel.myRentalList.collectAsStateWithLifecycle()

    Column {
        Column(
            modifier = Modifier
                .padding(top = 40.dp)
                .screenHorizontalPadding()
        ) {
            TopSection()
            ProfileSection()
            InfoBox()
        }
        TabbedListSection(
            isFirstTabSelected = isFirstTabSelected,
            myProductList = myProductList,
            myRentList = myRentalList,
            onTabActive = { isFirstTabSelected = !isFirstTabSelected },
            onItemClick = { id ->
                moveScreen(
                    navHostController,
                    NavigationRoutes.NAVHOSTPRODUCTDETAIL + "/$id",
                    saveStateEnabled = true,
                    restoreStateEnabled = true
                )
            }
        )
    }
}

@Composable
fun TopSection() {
    Row(
        modifier = Modifier.padding(bottom = 35.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1F),
            text = stringResource(id = R.string.title_activity_my_page_tab),
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(modifier = Modifier.size(30.dp), onClick = { /*TODO*/ }) {
            Box {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bell),
                    contentDescription = stringResource(
                        id = R.string.content_description_for_ic_bell
                    )
                )
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(AppRed)
                        .align(Alignment.TopEnd)
                )
            }

        }
        IconButton(modifier = Modifier.size(30.dp), onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_setting),
                contentDescription = stringResource(
                    id = R.string.content_description_for_ic_setting
                )
            )
        }
    }
}

@Composable
fun ProfileSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape),
            model = "url",
            placeholder = painterResource(id = R.drawable.img_profile_placeholder),
            error = painterResource(id = R.drawable.img_profile_placeholder),
            contentDescription = stringResource(id = R.string.content_description_for_img_profile_placeholder),
            contentScale = ContentScale.Crop
        )
        Text(
            modifier = Modifier
                .weight(1F)
                .padding(start = 15.dp),
            text = "닉네임",
            style = MaterialTheme.typography.bodyMedium
        )
        Box(modifier = Modifier.basicRoundedGrayBorder()) {
            Row(
                modifier = Modifier.padding(10.5.dp, 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.screen_mypage_btn_my_activity),
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryBlue500
                )
                Text(
                    text = stringResource(id = R.string.screen_mypage_btn_new),
                    modifier = Modifier.padding(start = 6.dp),
                    fontFamily = pretendardFamily,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AppRed
                )
            }
        }
    }
}

@Composable
fun InfoBox() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 26.dp, bottom = 13.dp)
            .basicRoundedGrayBorder()
            .padding(16.dp, 12.dp)
    ) {

        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.screen_mypage_info_text_start, "[상품명]"))
                withStyle(style = SpanStyle(color = PrimaryBlue500)) {
                    append(" 1일 ")
                }
                append(stringResource(R.string.screen_mypage_info_text_end))
            }, style = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier
                .padding(top = 4.dp)
                .clickable { },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.padding(end = 5.dp),
                text = stringResource(id = R.string.screen_mypage_check_usage_history),
                style = MaterialTheme.typography.bodySmall,
                color = PrimaryBlue500
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_right),
                contentDescription = stringResource(id = R.string.content_description_for_ic_chevron_right),
                tint = PrimaryBlue500
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TabbedListSection(
    isFirstTabSelected: Boolean,
    myProductList: List<ProductDto>,
    myRentList: List<ReservationDto>,
    onTabActive: () -> Unit,
    onItemClick: (Int) -> Unit,
) {
    Row {
        TabTitle(
            stringResource(id = R.string.screen_mypage_tab_title_my_product),
            isFirstTabSelected,
            Modifier.weight(1F),
            onClick = onTabActive
        )
        TabTitle(
            stringResource(id = R.string.screen_mypage_tab_title_on_rent),
            !isFirstTabSelected,
            Modifier.weight(1F),
            onClick = onTabActive
        )
    }
    CommonDivider()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray200),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val list = if (isFirstTabSelected) myProductList else myRentList
        if (list.isNotEmpty()) {
            if (isFirstTabSelected) {
                items(myProductList) {
                    ProductListItem(it, true) { onItemClick(it.productId) }
                }
            } else {
                items(myRentList) {
                    MyRentalHistoryListItem(it)
                }
            }
        } else {
            item {
                Column(
                    modifier = Modifier.padding(top = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(112.dp),
                        painter = painterResource(id = R.drawable.img_empty_box),
                        contentDescription = stringResource(id = R.string.content_description_for_img_empty_box)
                    )
                    Text(
                        modifier = Modifier.padding(top = 14.dp),
                        text = stringResource(id = R.string.screen_mypage_text_tab_list_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gray400
                    )
                }
            }
        }
    }
}

@Composable
fun TabTitle(title: String, isTabSelected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp, top = 13.dp),
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        if (isTabSelected) {
            Box(
                modifier = Modifier
                    .width(75.dp)
                    .height(2.dp)
                    .background(PrimaryBlue500)
            )
        }
    }
}
