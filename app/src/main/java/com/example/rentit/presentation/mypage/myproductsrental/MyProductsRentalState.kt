package com.example.rentit.presentation.mypage.myproductsrental

import com.example.rentit.common.D_DAY_ALERT_THRESHOLD_DAYS
import com.example.rentit.domain.user.model.MyProductsRentalModel
import com.example.rentit.presentation.mypage.myproductsrental.model.MyProductsRentalFilter

data class MyProductsRentalState(
    val rentalHistories: Map<MyProductsRentalFilter, List<MyProductsRentalModel>> = emptyMap(),
    val selectedFilter: MyProductsRentalFilter = MyProductsRentalFilter.WAITING_FOR_RESPONSE,
    val isLoading: Boolean = false,
    val showNoticeBanner: Boolean = true
) {
    val filteredRentalHistories: List<MyProductsRentalModel>
        get() = rentalHistories[selectedFilter] ?: emptyList()

    val upcomingShipmentCount: Int
        get() = rentalHistories[MyProductsRentalFilter.WAITING_FOR_SHIPMENT]
            ?.count { it.daysBeforeStart <= D_DAY_ALERT_THRESHOLD_DAYS } ?: 0

}