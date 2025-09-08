package com.example.rentit.presentation.mypage

import com.example.rentit.domain.user.model.MyProductItemModel
import com.example.rentit.domain.user.model.MyRentalItemModel
import com.example.rentit.domain.user.model.NearestDueItemModel

data class MyPageState(
    val profileImgUrl: String? = "",
    val nickName: String = "",
    val myProductCount: Int = 0,
    val myValidRentalCount: Int = 0,
    val myPendingRentalCount: Int = 0,
    val nearestDueItem: NearestDueItemModel? = null,
    val myProductList: List<MyProductItemModel> = emptyList(),
    val myRentalList: List<MyRentalItemModel> = emptyList(),
    val isFirstTabSelected: Boolean = true,
    val isLoading: Boolean = false,
    val showNetworkErrorDialog: Boolean = false,
    val showServerErrorDialog: Boolean = false
)