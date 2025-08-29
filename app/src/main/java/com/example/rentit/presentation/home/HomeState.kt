package com.example.rentit.presentation.home

import com.example.rentit.data.product.model.ProductWithCategory

data class HomeState(
    val productList: List<ProductWithCategory> = emptyList(),
    val parentCategoryNames: List<String> = emptyList(),
    val selectedCategoryName: String = "",
    val isLoading: Boolean = false,
    val onlyRentalAvailable: Boolean = false,
    val showServerErrorDialog: Boolean = false,
    val showNetworkErrorDialog: Boolean = false,
)