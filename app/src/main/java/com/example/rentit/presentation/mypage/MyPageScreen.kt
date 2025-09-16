package com.example.rentit.presentation.mypage

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.ArrowedTextButton
import com.example.rentit.common.component.CommonDivider
import com.example.rentit.common.component.LoadableUrlImage
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.AppRed
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.component.item.ProductListItem
import com.example.rentit.common.component.layout.EmptyContentScreen
import com.example.rentit.common.component.layout.PullToRefreshLayout
import com.example.rentit.common.enums.ProductStatus
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.formatRentalPeriod
import com.example.rentit.common.util.toRelativeTimeFormat
import com.example.rentit.domain.rental.model.RentingStatus
import com.example.rentit.domain.user.model.MyProductItemModel
import com.example.rentit.domain.user.model.MyRentalItemModel
import com.example.rentit.domain.user.model.NearestDueItemModel
import java.time.LocalDateTime
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyPageScreen(
    profileImgUrl: String?,
    nickName: String = "",
    myProductCount: Int = 0,
    myRentingCount: Int = 0,
    myPendingRentalCount: Int = 0,
    nearestDueItem: NearestDueItemModel?,
    myProductList: List<MyProductItemModel>,
    myRentalList: List<MyRentalItemModel>,
    pullToRefreshState: PullToRefreshState = PullToRefreshState(),
    isFirstTabSelected: Boolean,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onSettingClick: () -> Unit,
    onAlertClick: () -> Unit,
    onMyPendingRentalClick: () -> Unit,
    onInfoRentalDetailClick: () -> Unit,
    onTabActive: () -> Unit,
    onProductItemClick: (Int) -> Unit,
    onRentalItemClick: (Int, Int) -> Unit
) {
    PullToRefreshLayout(
        isRefreshing = isRefreshing,
        pullToRefreshState = pullToRefreshState,
        onRefresh = onRefresh
    ) {
        Column {
            Column(modifier = Modifier.screenHorizontalPadding()) {

                TopSection(onAlertClick, onSettingClick)

                ProfileSection(
                    profileImgUrl = profileImgUrl,
                    nickName = nickName,
                    myProductCount = myProductCount,
                    myRentingCount = myRentingCount,
                    myPendingRentalCount = myPendingRentalCount,
                    onMyPendingRentalClick = onMyPendingRentalClick
                )

                if (nearestDueItem != null) {
                    InfoBox(
                        productTitle = nearestDueItem.productTitle,
                        remainingRentalDays = nearestDueItem.remainingRentalDays,
                        onRentalDetailClick = onInfoRentalDetailClick
                    )
                }
            }

            TabbedListSection(
                 isFirstTabSelected = isFirstTabSelected,
                myProductList = myProductList,
                myRentList = myRentalList,
                onTabActive = onTabActive,
                onProductItemClick = onProductItemClick,
                onRentalItemClick = onRentalItemClick
            )
        }
    }
}

@Composable
fun TopSection(onAlertClick: () -> Unit = {}, onSettingClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.padding(top = 40.dp, bottom = 35.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1F),
            text = stringResource(id = R.string.title_activity_my_page_tab),
            style = MaterialTheme.typography.bodyLarge
        )
        IconButton(modifier = Modifier.padding(end = 8.dp).size(30.dp), onClick = onAlertClick) {
            Box {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bell),
                    contentDescription = stringResource(id = R.string.content_description_for_ic_bell)
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
        IconButton(modifier = Modifier.size(30.dp), onClick = onSettingClick) {
            Icon(
                painter = painterResource(id = R.drawable.ic_setting),
                contentDescription = stringResource(id = R.string.content_description_for_ic_setting)
            )
        }
    }
}

@Composable
fun ProfileSection(
    profileImgUrl: String? = null,
    nickName: String = "",
    myProductCount: Int = 0,
    myRentingCount: Int = 0,
    myPendingRentalCount: Int = 0,
    onMyPendingRentalClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.padding(bottom = 26.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        LoadableUrlImage(
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape),
            imgUrl = profileImgUrl,
            defaultImageResId = R.drawable.img_profile_placeholder,
            defaultDescResId = R.string.content_description_for_img_profile_placeholder
        )
        Column(modifier = Modifier
            .weight(1f)
            .padding(start = 20.dp)) {
            Text(
                text = nickName,
                style = MaterialTheme.typography.bodyMedium
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CountBox(stringResource(R.string.screen_mypage_my_activity_count_label_post), myProductCount)
                CountBox(stringResource(R.string.screen_mypage_my_activity_count_label_rental), myRentingCount)
                CountBox(stringResource(R.string.screen_mypage_my_activity_count_label_pending), myPendingRentalCount, true, onMyPendingRentalClick)
            }
        }
    }
}

@Composable
fun RowScope.CountBox(label: String, count: Int, clickable: Boolean = false, onClick: () -> Unit = {}) {
    Column(modifier = Modifier
        .weight(1f)
        .clip(RoundedCornerShape(4.dp))
        .clickable(clickable) { onClick() }) {
        Text(
            modifier = Modifier.padding(bottom = 2.dp),
            text = count.toString(),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Gray400
        )
    }
}

@Composable
fun InfoBox(
    productTitle: String = "",
    remainingRentalDays: Int = 0,
    onRentalDetailClick: () -> Unit = {}
) {
    val rentingStatus = RentingStatus.fromDaysFromReturnDate(remainingRentalDays)
    val highlightColor = if(rentingStatus == RentingStatus.RENTING_IN_USE) PrimaryBlue500 else rentingStatus.textColor
    val infoText = getRentalInfoText(rentingStatus, remainingRentalDays, highlightColor)
    val buttonText = stringResource(
        when(rentingStatus) {
            RentingStatus.RENTING_IN_USE -> R.string.screen_mypage_info_button_rental_detail
            RentingStatus.RENTING_OVERDUE, RentingStatus.RENTING_RETURN_DAY -> R.string.screen_mypage_info_button_return
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Gray100)
            .padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            if(rentingStatus == RentingStatus.RENTING_RETURN_DAY) {
                Text(
                    text = stringResource(R.string.screen_mypage_info_text_return_day_1) + " ",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Text(
                modifier = Modifier.weight(1f, fill = false),
                text = "$productTitle ",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.labelLarge
            )
            Text(
                text = infoText,
                style = MaterialTheme.typography.labelMedium
            )
        }
        ArrowedTextButton(
            text = buttonText,
            color = highlightColor,
            onClick = onRentalDetailClick
        )
    }
}

@Composable
fun getRentalInfoText(rentingStatus: RentingStatus, remainingRentalDays: Int, highlightColor: Color): AnnotatedString {
    return when (rentingStatus) {
        RentingStatus.RENTING_IN_USE, RentingStatus.RENTING_OVERDUE -> {
            val endTextRes = if(rentingStatus == RentingStatus.RENTING_IN_USE) R.string.screen_mypage_info_text_left else R.string.screen_mypage_info_text_overdue
            buildAnnotatedString {
                append(stringResource(R.string.screen_mypage_info_text_1))
                withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle().copy(color = highlightColor)) {
                    append(" ${abs(remainingRentalDays)}${stringResource(R.string.common_day_unit)} ")
                }
                append(stringResource(endTextRes))
            }
        }
        RentingStatus.RENTING_RETURN_DAY -> {
            buildAnnotatedString {
                append(stringResource(R.string.screen_mypage_info_text_return_day_2))
                withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle().copy(color = rentingStatus.textColor)) {
                    append(" " + stringResource(R.string.screen_mypage_info_text_return_day_3))
                }
                append(stringResource(R.string.screen_mypage_info_text_return_day_4))
            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TabbedListSection(
    isFirstTabSelected: Boolean,
    myProductList: List<MyProductItemModel>,
    myRentList: List<MyRentalItemModel>,
    onTabActive: () -> Unit,
    onProductItemClick: (Int) -> Unit,
    onRentalItemClick: (Int, Int) -> Unit,
) {
    Row(modifier = Modifier.padding(top = 12.dp)) {
        TabTitle(
            modifier = Modifier.weight(1F),
            title = stringResource(id = R.string.screen_mypage_tab_title_my_product),
            isTabSelected = isFirstTabSelected,
            onClick = onTabActive
        )
        TabTitle(
            modifier = Modifier.weight(1F),
            title = stringResource(id = R.string.screen_mypage_tab_title_on_rent),
            isTabSelected = !isFirstTabSelected,
            onClick = onTabActive
        )
    }

    CommonDivider()

    if (isFirstTabSelected && myProductList.isNotEmpty()) {
        LazyColumn(modifier = Modifier.background(Gray100)) {
            items(myProductList, key = { it.productId }) {
                ProductListItem(
                    title = it.title,
                    price = it.price,
                    thumbnailImgUrl = it.thumbnailImgUrl,
                    minPeriod = it.minPeriod,
                    maxPeriod = it.maxPeriod,
                    categories = it.categories,
                    status = it.status,
                    createdAt = it.createdAt,
                ) { onProductItemClick(it.productId) }
            }
        }
    } else if(!isFirstTabSelected && myRentList.isNotEmpty()) {
        LazyColumn(modifier = Modifier.background(Gray100)) {
            items(myRentList, key = { it.reservationId }) {
                MyRentalHistoryListItem(
                    title = it.productTitle,
                    thumbnailImgUrl = it.thumbnailImgUrl,
                    startDate = it.startDate,
                    endDate = it.endDate,
                    status = it.status,
                    requestedAt = it.requestedAt
                ) { onRentalItemClick(it.productId, it.reservationId) }
            }
        }
    } else {
        val text =
            if(isFirstTabSelected) {
                stringResource(id = R.string.screen_mypage_text_tab_list_product_empty)
            } else {
                stringResource(id = R.string.screen_mypage_text_tab_list_rental_empty)
            }
        EmptyContentScreen(
            modifier = Modifier.background(Gray100),
            text = text
        )
    }
}

@Composable
fun TabTitle(modifier: Modifier, title: String, isTabSelected: Boolean, onClick: () -> Unit) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(top = 12.dp, bottom = 10.dp),
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        if (isTabSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(2.dp)
                    .background(PrimaryBlue500)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyRentalHistoryListItem(
    title: String,
    thumbnailImgUrl: String?,
    startDate: String,
    endDate: String,
    status: RentalStatus,
    requestedAt: String,
    onItemClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable { onItemClick() }
            .screenHorizontalPadding()
            .padding(vertical = 25.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LoadableUrlImage(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(20.dp)),
            imgUrl = thumbnailImgUrl,
            defaultImageResId = R.drawable.img_thumbnail_placeholder,
        )
        Column(Modifier.padding(start = 18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = stringResource(status.strRes),
                    style = MaterialTheme.typography.labelMedium,
                    color = status.color
                )
            }
            Text(
                modifier = Modifier.padding(top = 4.dp, bottom = 10.dp),
                text = LocalDateTime.parse(requestedAt).toRelativeTimeFormat(),
                style = MaterialTheme.typography.labelMedium,
                color = Gray400
            )
            Text(
                text = formatRentalPeriod(LocalContext.current, startDate, endDate),
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun MyPageScreenPreview() {

    val sampleNearestDueItem = NearestDueItemModel(
        reservationId = 1,
        productId = 101,
        productTitle = "아이패드 프로 12.9인치",
        remainingRentalDays = 1
    )

    val sampleMyProductList = listOf(
        MyProductItemModel(
            productId = 101,
            title = "아이패드 프로 12.9인치",
            price = 120000,
            thumbnailImgUrl = "https://example.com/ipad.jpg",
            minPeriod = 1,
            maxPeriod = 7,
            categories = listOf("전자기기", "태블릿"),
            status = ProductStatus.AVAILABLE,
            createdAt = "2025-09-08T12:00:00"
        )
    )

    val sampleMyRentalList = listOf(
        MyRentalItemModel(
            productId = 102,
            reservationId = 201,
            productTitle = "맥북 에어 M2",
            thumbnailImgUrl = "https://example.com/macbook.jpg",
            startDate = "2025-09-05",
            endDate = "2025-09-12",
            status = RentalStatus.RENTING,
            requestedAt = "2025-09-04T12:30:00"
        )
    )

    RentItTheme {
        MyPageScreen(
            profileImgUrl = "https://example.com/profile.jpg",
            nickName = "홍길동",
            myProductCount = sampleMyProductList.size,
            myRentingCount = sampleMyRentalList.size,
            myPendingRentalCount = 0,
            nearestDueItem = sampleNearestDueItem,
            myProductList = sampleMyProductList,
            myRentalList = sampleMyRentalList,
            isFirstTabSelected = true,
            onSettingClick = {},
            onAlertClick = {},
            onMyPendingRentalClick = {},
            onInfoRentalDetailClick = {},
            onTabActive = {},
            onProductItemClick = {},
            onRentalItemClick = { _, _ -> }
        )
    }
}