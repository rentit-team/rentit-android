package com.example.rentit.presentation.mypage.myproductsrental

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.D_DAY_ALERT_THRESHOLD_DAYS
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.FilterButton
import com.example.rentit.common.component.LoadableUrlImage
import com.example.rentit.common.component.AnimatedNoticeBanner
import com.example.rentit.common.component.layout.EmptyContentScreen
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.theme.AppRed
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.PrimaryBlue500
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.formatPrice
import com.example.rentit.domain.user.model.MyProductsRentalModel
import com.example.rentit.presentation.mypage.myproductsrental.model.MyProductsRentalFilter
import kotlin.math.abs

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyProductsRentalScreen(
    rentals: List<MyProductsRentalModel> = emptyList(),
    selectedFilter: MyProductsRentalFilter = MyProductsRentalFilter.WAITING_FOR_RESPONSE,
    showNoticeBanner: Boolean = true,
    onFilterChange: (MyProductsRentalFilter) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.screen_my_products_rental_title),
                onBackClick = onBackClick
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .screenHorizontalPadding()
        ) {
            FilterSection(selectedFilter, onFilterChange)

            if(showNoticeBanner) {
                NoticeBannerSection(selectedFilter)
            }

            RentalHistoriesSection(selectedFilter, rentals)
        }
    }
}

@Composable
fun FilterSection(
    selectedFilter: MyProductsRentalFilter = MyProductsRentalFilter.WAITING_FOR_RESPONSE,
    onFilterChange: (MyProductsRentalFilter) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(vertical = 20.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FilterButton(
            title = stringResource(R.string.screen_my_products_rental_filter_waiting_for_response),
            contentDesc = "",
            isSelected = selectedFilter == MyProductsRentalFilter.WAITING_FOR_RESPONSE,
            onClick = { onFilterChange(MyProductsRentalFilter.WAITING_FOR_RESPONSE) }
        )
        FilterButton(
            title = stringResource(R.string.screen_my_products_rental_filter_waiting_for_shipment),
            contentDesc = "",
            isSelected = selectedFilter == MyProductsRentalFilter.WAITING_FOR_SHIPMENT,
            onClick = { onFilterChange(MyProductsRentalFilter.WAITING_FOR_SHIPMENT) }
        )
        FilterButton(
            title = stringResource(R.string.screen_my_products_rental_filter_renting),
            contentDesc = "",
            isSelected = selectedFilter == MyProductsRentalFilter.RENTING,
            onClick = { onFilterChange(MyProductsRentalFilter.RENTING) }
        )
        FilterButton(
            title = stringResource(R.string.screen_my_products_rental_filter_accepted),
            contentDesc = "",
            isSelected = selectedFilter == MyProductsRentalFilter.ACCEPTED,
            onClick = { onFilterChange(MyProductsRentalFilter.ACCEPTED) }
        )
    }
}

@Composable
fun NoticeBannerSection(selectedFilter: MyProductsRentalFilter) {
    val noticeText =
        buildAnnotatedString {
            when (selectedFilter) {
                MyProductsRentalFilter.WAITING_FOR_RESPONSE -> {
                    withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle().copy(PrimaryBlue500)) {
                        append(stringResource(R.string.screen_my_products_rental_notice_waiting_for_response_1))
                    }
                    append(stringResource(R.string.screen_my_products_rental_notice_waiting_for_response_2))
                }

                MyProductsRentalFilter.WAITING_FOR_SHIPMENT -> {
                    append(stringResource(R.string.screen_my_products_rental_notice_waiting_for_shipment_1))
                    withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle().copy(PrimaryBlue500)) {
                        append("0 " + stringResource(R.string.common_case_unit))
                    }
                    append(stringResource(R.string.screen_my_products_rental_notice_waiting_for_shipment_2))
                }

                else -> { }
        }
    }
    AnimatedNoticeBanner(
        modifier = Modifier.padding(bottom = 10.dp),
        noticeText = noticeText
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoriesSection(
    selectedFilter: MyProductsRentalFilter = MyProductsRentalFilter.WAITING_FOR_RESPONSE,
    rentals: List<MyProductsRentalModel> = emptyList()
) {
    if(rentals.isEmpty()) return EmptyContentScreen(text = stringResource(R.string.screen_my_products_rental_empty_list))

    LazyColumn {
        items(rentals.size) { i ->
            RentalHistoryItem(
                selectedFilter = selectedFilter,
                productTitle = rentals[i].productTitle,
                rentalCount = rentals[i].rentalCount,
                totalExpectRevenue = rentals[i].totalExpectRevenue,
                renterNickname = rentals[i].renterNickname,
                daysBeforeStart = rentals[i].daysBeforeStart,
                daysBeforeReturn = rentals[i].daysBeforeReturn
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryItem(
    selectedFilter: MyProductsRentalFilter = MyProductsRentalFilter.WAITING_FOR_RESPONSE,
    productTitle: String = "",
    rentalCount: Int = 0,
    totalExpectRevenue: Int = 0,
    renterNickname: String = "",
    daysBeforeStart: Int = 0,
    daysBeforeReturn: Int = 0
) {
    val rentalInfoText = getRentalInfoText(
        selectedFilter = selectedFilter,
        rentalCount = rentalCount,
        totalExpectRevenue = totalExpectRevenue,
        renterNickname = renterNickname,
        daysBeforeStart = daysBeforeStart,
        daysBeforeReturn = daysBeforeReturn
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        LoadableUrlImage(
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .aspectRatio(1f),
            imgUrl = null,
        )

        Column {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = productTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = rentalInfoText,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun getRentalInfoText(
    selectedFilter: MyProductsRentalFilter = MyProductsRentalFilter.WAITING_FOR_RESPONSE,
    rentalCount: Int = 0,
    totalExpectRevenue: Int = 0,
    renterNickname: String = "",
    daysBeforeStart: Int = 0,
    daysBeforeReturn: Int = 0
): AnnotatedString {
    when (selectedFilter) {
        MyProductsRentalFilter.WAITING_FOR_RESPONSE -> {
            return buildAnnotatedString {
                withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle().copy(PrimaryBlue500)) {
                    append("$rentalCount " + stringResource(R.string.common_case_unit))
                }
                append(stringResource(R.string.screen_my_products_rental_info_text_waiting_for_response))
                withStyle(style = MaterialTheme.typography.labelMedium.toSpanStyle().copy(Gray400)) {
                    append(" · " + stringResource(R.string.common_total) + " ${formatPrice(totalExpectRevenue)}" + stringResource(R.string.common_price_unit))
                }
            }
        }
        MyProductsRentalFilter.WAITING_FOR_SHIPMENT -> {
            val dDayText = if(daysBeforeStart >= 0) " D-$daysBeforeStart" else " D+${abs(daysBeforeStart)}"
            val dDayColor = if(daysBeforeStart > D_DAY_ALERT_THRESHOLD_DAYS) PrimaryBlue500 else AppRed
            return buildAnnotatedString {
                withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {
                    append("$renterNickname ")
                }
                append(stringResource(R.string.screen_my_products_rental_info_text_waiting_for_shipment))
                withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle().copy(dDayColor)) {
                    append(dDayText)
                }
            }
        }
        MyProductsRentalFilter.RENTING -> {
            val dDayText = if(daysBeforeReturn >= 0) " D-$daysBeforeReturn" else " D+${abs(daysBeforeReturn)}"
            val dDayColor = if(daysBeforeReturn > D_DAY_ALERT_THRESHOLD_DAYS) PrimaryBlue500 else AppRed
            return buildAnnotatedString {
                withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {
                    append("$renterNickname ")
                }
                append(stringResource(R.string.screen_my_products_rental_info_text_renting))
                withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle().copy(dDayColor)) {
                    append(dDayText)
                }
            }
        }
        MyProductsRentalFilter.ACCEPTED -> {
            return buildAnnotatedString {
                withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle().copy(PrimaryBlue500)) {
                    append("$rentalCount " + stringResource(R.string.common_case_unit))
                }
                append(stringResource(R.string.screen_my_products_rental_info_text_accepted))
                withStyle(style = MaterialTheme.typography.labelMedium.toSpanStyle().copy(Gray400)) {
                    append(" · " + stringResource(R.string.common_total) + " ${formatPrice(totalExpectRevenue)}" + stringResource(R.string.common_price_unit))
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun MyProductsRentalPreview() {
    RentItTheme {
        MyProductsRentalScreen(
            rentals = emptyList(),
            selectedFilter = MyProductsRentalFilter.WAITING_FOR_RESPONSE,
            onFilterChange = {},
            onBackClick = {}
        )
    }
}