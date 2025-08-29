package com.example.rentit.presentation.home

import com.example.rentit.common.enums.ProductStatus
import com.example.rentit.data.product.model.Category
import com.example.rentit.data.product.model.ProductWithCategory

data class HomeState(
    val productList: List<ProductWithCategory> = emptyList(),
    val categoryMap: Map<Int, Category> = emptyMap(),
    val parentIdToNameCategoryMap: Map<Int, String> = emptyMap(),
    val filterParentCategoryId: Int = -1,
    val isLoading: Boolean = false,
    val onlyRentalAvailable: Boolean = false,
    val showServerErrorDialog: Boolean = false,
    val showNetworkErrorDialog: Boolean = false,
) {
    val filteredProductList: List<ProductWithCategory>
        get() = productList
            .filter {
                val matchesCategory =
                    filterParentCategoryId == -1 || filterParentCategoryId in it.parentCategoryIds
                val matchesRentalStatus =
                    !onlyRentalAvailable || it.status == ProductStatus.AVAILABLE
                matchesCategory && matchesRentalStatus
            }
}