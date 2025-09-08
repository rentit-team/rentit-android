package com.example.rentit.domain.user.model

data class MyRentalsWithNearestDueModel(
    val myRentalList: List<MyRentalItemModel>,
    val myValidRentalCount: Int,
    val nearestDueItem: NearestDueItemModel?
)