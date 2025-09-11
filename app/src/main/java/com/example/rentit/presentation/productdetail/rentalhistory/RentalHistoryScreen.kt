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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.rentit.common.component.calendar.CalendarDate
import com.example.rentit.common.component.calendar.CalendarHeader
import com.example.rentit.common.component.calendar.DayOfWeek
import com.example.rentit.common.component.screenHorizontalPadding
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.common.theme.AppBlack
import com.example.rentit.common.theme.AppRed
import com.example.rentit.common.theme.Gray100
import com.example.rentit.common.theme.Gray400
import com.example.rentit.common.theme.RentItTheme
import com.example.rentit.common.util.formatRentalPeriod
import com.example.rentit.common.util.toRelativeTimeFormat
import com.example.rentit.common.uimodel.RentalPeriodModel
import com.example.rentit.common.util.daysFromToday
import com.example.rentit.domain.rental.model.RentalHistoryListItemModel
import com.example.rentit.presentation.productdetail.rentalhistory.model.RentalHistoryFilter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

private const val D_DAY_ALERT_THRESHOLD_DAYS = 3

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryScreen(
    rentalHistoryList: List<RentalHistoryListItemModel>,
    filterMode: RentalHistoryFilter,
    calendarMonth: YearMonth,
    historyListScrollState: LazyListState,
    onToggleFilter: (RentalHistoryFilter) -> Unit,
    onChangeMonth: (Long) -> Unit = {},
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = { CommonTopAppBar(title = "대여 내역", onBackClick = onBackClick) }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            RentalHistoryCalendar(rentalHistoryList.map { history -> history.rentalPeriod }, calendarMonth, onChangeMonth)

            RentalHistoryFilterSection(
                filterMode = filterMode,
                onToggleFilter = onToggleFilter
            )

            RentalHistoryListSection(rentalHistoryList, historyListScrollState)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RentalHistoryCalendar(
    rentalPeriodList: List<RentalPeriodModel>,
    calendarMonth: YearMonth,
    onChangeMonth: (Long) -> Unit = {},
) {
    val cellWidth = 48.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CalendarHeader(calendarMonth, { onChangeMonth(-1) }, { onChangeMonth(1) })
        DayOfWeek(cellWidth)
        CalendarDate(
            yearMonth = calendarMonth,
            disabledDates = emptyList(),
            cellWidth = cellWidth,
            isPastDateDisabled = true,
            requestPeriodList = rentalPeriodList
        )
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
fun RentingListItem(nickName: String = "닉네임", rentalReturnDate: LocalDate = LocalDate.now()) {
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
                append("반납이 ")
                withStyle(style = MaterialTheme.typography.labelLarge.toSpanStyle()) {
                    append("$daysBeforeReturn 일")
                }
                append(" 남았어요!")
            },
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.End
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReadyToShipListItem(nickName: String = "닉네임", rentalStartDate: LocalDate? = LocalDate.now()) {
    val daysBeforeRent = daysFromToday(rentalStartDate)
    val dDayTextColor = if (daysBeforeRent > D_DAY_ALERT_THRESHOLD_DAYS) AppBlack else AppRed
    RoundedItemRow {
        Text(
            text = stringResource(RentalStatus.PAID.strRes),
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
                append("대여 시작일까지 ")
                withStyle(
                    style = MaterialTheme.typography.labelLarge.toSpanStyle()
                        .copy(color = dDayTextColor)
                ) {
                    append("D - $daysBeforeRent")
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
    nickName: String = "닉네임",
    rentalStartDate: LocalDate = LocalDate.now(),
    rentalEndDate: LocalDate = LocalDate.now(),
    createdAt: String = "2025-08-30T14:22:28"
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
                    text = LocalDateTime.parse(createdAt).toRelativeTimeFormat(),
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
    rentalHistoryList: List<RentalHistoryListItemModel> = emptyList(),
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
                        nickName = history.nickname,
                        rentalReturnDate = history.rentalPeriod.endDate
                    )
                }

                RentalStatus.PAID -> {
                    ReadyToShipListItem(
                        nickName = history.nickname,
                        rentalStartDate = history.rentalPeriod.startDate
                    )
                }

                else -> {
                    OtherStatusListItem(
                        status = history.status,
                        nickName = history.nickname,
                        rentalStartDate = history.rentalPeriod.startDate,
                        rentalEndDate = history.rentalPeriod.endDate,
                        createdAt = history.createdAt
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

