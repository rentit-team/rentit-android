package com.example.rentit.presentation.productdetail.rentalhistory

import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.domain.rental.model.RentalHistoryListItemModel
import com.example.rentit.presentation.productdetail.rentalhistory.model.RentalHistoryFilter

data class RentalHistoryState(
    val isLoading: Boolean = false,
    val filterMode: RentalHistoryFilter = RentalHistoryFilter.IN_PROGRESS,
    val rentalHistoryList: List<RentalHistoryListItemModel> = emptyList(),
) {
    val filteredRentalHistoryList: List<RentalHistoryListItemModel>
        get() {

            when (filterMode) {
                RentalHistoryFilter.IN_PROGRESS -> {
                    val rentingList = rentalHistoryList.filter { it.status == RentalStatus.RENTING }
                    val readyToShipList = rentalHistoryList.filter { it.status == RentalStatus.PAID }.sortedBy { it.rentalPeriod.startDate }
                    val otherList = rentalHistoryList.filter {
                        it.status in listOf(
                            RentalStatus.ACCEPTED,
                            RentalStatus.PENDING
                        )
                    }.sortedByDescending { it.createdAt }
                    return rentingList + readyToShipList + otherList
                }

                RentalHistoryFilter.ACCEPTED -> return rentalHistoryList.filter { it.status == RentalStatus.ACCEPTED }.sortedByDescending { it.createdAt }

                RentalHistoryFilter.REQUEST -> return rentalHistoryList.filter { it.status == RentalStatus.PENDING }.sortedByDescending { it.createdAt }

                RentalHistoryFilter.FINISHED -> return rentalHistoryList.filter {
                    it.status in listOf(
                        RentalStatus.CANCELED,
                        RentalStatus.REJECTED,
                        RentalStatus.RETURNED
                    )
                }.sortedByDescending { it.createdAt }
            }
        }
}