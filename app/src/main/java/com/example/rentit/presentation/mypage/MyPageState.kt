package com.example.rentit.presentation.mypage

import com.example.rentit.domain.user.model.MyProductItemModel
import com.example.rentit.domain.user.model.MyRentalItemModel
import com.example.rentit.domain.user.model.NearestDueItemModel
import com.example.rentit.presentation.mypage.model.MyPageTab

data class MyPageState(
    val currentTab: MyPageTab = MyPageTab.MY_PRODUCT,
    val profileImgUrl: String? = "",
    val nickName: String = "",
    val myProductCount: Int = 0,
    val myValidRentalCount: Int = 0,
    val myPendingRentalCount: Int = 0,
    val nearestDueItem: NearestDueItemModel? = null,
    val myProductList: List<MyProductItemModel> = emptyList(),
    val myRentalList: List<MyRentalItemModel> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
)