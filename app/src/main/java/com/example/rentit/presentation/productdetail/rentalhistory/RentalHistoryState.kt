package com.example.rentit.presentation.productdetail.rentalhistory

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyListState
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.presentation.productdetail.rentalhistory.model.RentalHistoryDateModel
import com.example.rentit.domain.rental.model.RentalHistoryModel
import com.example.rentit.presentation.productdetail.rentalhistory.model.RentalHistoryFilter
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
data class RentalHistoryState(
    val isLoading: Boolean = false,
    val calendarMonth: YearMonth = YearMonth.now(),
    val historyListScrollState: LazyListState = LazyListState(),
    val selectedReservationId: Int? = null,
    val filterMode: RentalHistoryFilter = RentalHistoryFilter.IN_PROGRESS,
    val rentalHistoryList: List<RentalHistoryModel> = emptyList(),
    val showAccessForbiddenDialog: Boolean = false
) {

    val isListItemExpanded: Boolean
        get() = selectedReservationId != null

    val filteredRentalHistoryList: List<RentalHistoryModel>
        get() {
            selectedReservationId?.let { id ->
                return rentalHistoryList.filter { it.reservationId == id }
            }
            when (filterMode) {
                RentalHistoryFilter.IN_PROGRESS -> {
                    val rentingList = rentalHistoryList.filter { it.status == RentalStatus.RENTING }
                    val readyToShipList = rentalHistoryList.filter { it.status == RentalStatus.PAID }.sortedBy { it.rentalPeriod.startDate }
                    val otherList = rentalHistoryList.filter {
                        it.status in listOf(
                            RentalStatus.ACCEPTED,
                            RentalStatus.PENDING
                        )
                    }.sortedByDescending { it.requestedAt }
                    return rentingList + readyToShipList + otherList
                }

                RentalHistoryFilter.ACCEPTED -> return rentalHistoryList.filter { it.status == RentalStatus.ACCEPTED }.sortedByDescending { it.requestedAt }

                RentalHistoryFilter.REQUEST -> return rentalHistoryList.filter { it.status == RentalStatus.PENDING }.sortedByDescending { it.requestedAt }

                RentalHistoryFilter.FINISHED -> return rentalHistoryList.filter {
                    it.status in listOf(
                        RentalStatus.CANCELED,
                        RentalStatus.REJECTED,
                        RentalStatus.RETURNED
                    )
                }.sortedByDescending { it.requestedAt }
            }
        }

    val rentalHistoryByDateMap: Map<LocalDate, RentalHistoryDateModel>
        get() {
            val rentalHistoryByDate = mutableMapOf<LocalDate, RentalHistoryDateModel>()
            filteredRentalHistoryList
                .filter {
                    val start = it.rentalPeriod.startDate
                    val end = it.rentalPeriod.endDate
                    start != null && end != null && !start.isAfter(end)
                }
                .reversed()
                .forEach {
                    val startDate = it.rentalPeriod.startDate
                    val endDate = it.rentalPeriod.endDate

                    generateSequence(startDate) { date ->
                        val next = date.plusDays(1)
                        if (next <= endDate) next else null
                    }.forEach { date ->
                        rentalHistoryByDate[date] = RentalHistoryDateModel(
                            reservationId = it.reservationId,
                            rentalStatus = it.status
                        )
                    }
                }
            return rentalHistoryByDate.toMap()
        }
}