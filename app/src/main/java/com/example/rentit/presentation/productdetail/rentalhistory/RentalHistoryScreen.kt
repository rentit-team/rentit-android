package com.example.rentit.presentation.productdetail.rentalhistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rentit.R
import com.example.rentit.common.component.CommonTopAppBar
import com.example.rentit.common.component.FilterButton
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.AppRed
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.formatRentalPeriod
import com.example.rentit.common.util.toRelativeTimeFormat
import com.example.rentit.common.util.daysFromToday
import com.example.rentit.presentation.productdetail.rentalhistory.model.RentalHistoryDateModel
import com.example.rentit.domain.rental.model.RentalHistoryModel
import com.example.rentit.presentation.productdetail.rentalhistory.calendar.RentalHistoryCalendar
import com.example.rentit.presentation.productdetail.rentalhistory.model.RentalHistoryFilter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

private const val D_DAY_ALERT_THRESHOLD_DAYS = 3

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryScreen(
    rentalHistoryList: List<RentalHistoryModel>,
    rentalHistoryByDateMap: Map<LocalDate, RentalHistoryDateModel>,
    filterMode: RentalHistoryFilter,
    calendarMonth: YearMonth,
    historyListScrollState: LazyListState,
    onToggleFilter: (RentalHistoryFilter) -> Unit,
    onChangeMonth: (Long) -> Unit = {},
    onRentalDateClick: (Int) -> Unit = {},
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CommonTopAppBar(
                title = stringResource(R.string.screen_product_rental_history_title),
                onBackClick = onBackClick
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            RentalHistoryCalendar(rentalHistoryByDateMap, calendarMonth, onChangeMonth, onRentalDateClick)

            RentalHistoryFilterSection(
                filterMode = filterMode,
                onToggleFilter = onToggleFilter
            )

            RentalHistoryListSection(rentalHistoryList, historyListScrollState)
        }
    }
}

@Composable
fun RentalHistoryFilterSection(
    filterMode: RentalHistoryFilter = RentalHistoryFilter.IN_PROGRESS,
    onToggleFilter: (RentalHistoryFilter) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .screenHorizontalPadding()
            .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.spacedBy(9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterButton(
            title = stringResource(R.string.screen_product_rental_history_filter_in_progress),
            contentDesc = stringResource(R.string.screen_product_rental_history_filter_in_progress_content_description),
            isSelected = filterMode == RentalHistoryFilter.IN_PROGRESS,
            onClick = { onToggleFilter(RentalHistoryFilter.IN_PROGRESS) }
        )
        FilterButton(
            title = stringResource(R.string.screen_product_rental_history_filter_accepted),
            contentDesc = stringResource(R.string.screen_product_rental_history_filter_accepted_content_description),
            isSelected = filterMode == RentalHistoryFilter.ACCEPTED,
            onClick = { onToggleFilter(RentalHistoryFilter.ACCEPTED) }
        )
        FilterButton(
            title = stringResource(R.string.screen_product_rental_history_filter_request),
            contentDesc = stringResource(R.string.screen_product_rental_history_filter_request_content_description),
            isSelected = filterMode == RentalHistoryFilter.REQUEST,
            onClick = { onToggleFilter(RentalHistoryFilter.REQUEST) }
        )
        FilterButton(
            title = stringResource(R.string.screen_product_rental_history_filter_finished),
            contentDesc = stringResource(R.string.screen_product_rental_history_filter_finished_content_description),
            isSelected = filterMode == RentalHistoryFilter.FINISHED,
            onClick = { onToggleFilter(RentalHistoryFilter.FINISHED) }
        )
    }
}

@Composable
private fun RoundedItemRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(25.dp))
            .background(Gray100)
            .clickable { }
            .padding(horizontal = 30.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentingListItem(nickName: String = "", rentalReturnDate: LocalDate? = LocalDate.now()) {
    val daysBeforeReturn = daysFromToday(rentalReturnDate)
    RoundedItemRow {
        Text(
            text = stringResource(RentalStatus.RENTING.strRes),
            style = MaterialTheme.typography.labelLarge,
            color = RentalStatus.RENTING.color
        )
        Text(
            text = nickName,
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = buildAnnotatedString {
                append(stringResource(R.string.screen_product_rental_history_item_renting_info_1))
                withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {
                    append(" $daysBeforeReturn ${stringResource(R.string.common_day_unit)} ")
                }
                append(stringResource(R.string.screen_product_rental_history_item_renting_info_2))
            },
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.End
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReadyToShipListItem(nickName: String = "", rentalStartDate: LocalDate? = LocalDate.now()) {
    val daysBeforeRent = daysFromToday(rentalStartDate)
    val dDayTextColor = if (daysBeforeRent > D_DAY_ALERT_THRESHOLD_DAYS) AppBlack else AppRed
    RoundedItemRow {
        Text(
            text = stringResource(R.string.rental_status_paid_owner),
            style = MaterialTheme.typography.labelLarge,
            color = RentalStatus.PAID.color
        )
        Text(
            text = nickName,
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = buildAnnotatedString {
                append(stringResource(R.string.screen_product_rental_history_item_ready_to_ship_info))
                withStyle(
                    style = MaterialTheme.typography.labelLarge.toSpanStyle()
                        .copy(color = dDayTextColor)
                ) {
                    append(" D - $daysBeforeRent")
                }
            },
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.End
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OtherStatusListItem(
    status: RentalStatus = RentalStatus.RETURNED,
    nickName: String = "",
    rentalStartDate: LocalDate? = LocalDate.now(),
    rentalEndDate: LocalDate? = LocalDate.now(),
    requestedAt: LocalDateTime? = LocalDateTime.now()
) {
    RoundedItemRow {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Text(
                    text = stringResource(status.strRes),
                    style = MaterialTheme.typography.labelLarge,
                    color = status.color
                )
                Text(
                    text = nickName,
                    style = MaterialTheme.typography.labelMedium,
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = requestedAt?.toRelativeTimeFormat() ?: "",
                    style = MaterialTheme.typography.labelMedium,
                    color = Gray400,
                    textAlign = TextAlign.End
                )
            }
            Text(
                text = formatRentalPeriod(LocalContext.current, rentalStartDate, rentalEndDate),
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryListSection(
    rentalHistoryList: List<RentalHistoryModel> = emptyList(),
    lazyListState: LazyListState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .screenHorizontalPadding(),
        state = lazyListState,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(rentalHistoryList.size, key = { rentalHistoryList[it].reservationId }) {
            val history = rentalHistoryList[it]
            when (history.status) {
                RentalStatus.RENTING -> {
                    RentingListItem(
                        nickName = history.renterNickName,
                        rentalReturnDate = history.rentalPeriod.endDate
                    )
                }

                RentalStatus.PAID -> {
                    ReadyToShipListItem(
                        nickName = history.renterNickName,
                        rentalStartDate = history.rentalPeriod.startDate
                    )
                }

                else -> {
                    OtherStatusListItem(
                        status = history.status,
                        nickName = history.renterNickName,
                        rentalStartDate = history.rentalPeriod.startDate,
                        rentalEndDate = history.rentalPeriod.endDate,
                        requestedAt = history.requestedAt
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun RentalHistoryScreenPreview() {
    RentItTheme {
        RentalHistoryScreen(
            rentalHistoryList = emptyList(),
            rentalHistoryByDateMap = emptyMap(),
            filterMode = RentalHistoryFilter.ACCEPTED,
            calendarMonth = YearMonth.now(),
            historyListScrollState = remember { LazyListState() },
            onBackClick = {},
            onToggleFilter = {},
            onChangeMonth = {}
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun RentalHistoryListItemPreview() {
    RentItTheme {
        Column {
            RentingListItem()
            ReadyToShipListItem()
            OtherStatusListItem()
        }
    }
}

