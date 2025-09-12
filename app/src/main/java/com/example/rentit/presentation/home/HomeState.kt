package com.example.rentit.presentation.home

import androidx.compose.foundation.lazy.LazyListState
import com.example.rentit.common.enums.ProductStatus
import com.example.rentit.domain.product.model.CategoryModel
import com.example.rentit.domain.product.model.ProductWithCategoryModel

data class HomeState(
    val productList: List<ProductWithCategoryModel> = emptyList(),
    val categoryMap: Map<Int, CategoryModel> = emptyMap(),
    val parentIdToNameCategoryMap: Map<Int, String> = emptyMap(),
    val filterParentCategoryId: Int = -1,
    val scrollState: LazyListState = LazyListState(),
    val isLoading: Boolean = false,
    val onlyRentalAvailable: Boolean = false,
    val showServerErrorDialog: Boolean = false,
    val showNetworkErrorDialog: Boolean = false,
) {
    val filteredProductList: List<ProductWithCategoryModel>
        get() = productList
            .filter {
                val matchesCategory =
                    filterParentCategoryId == -1 || filterParentCategoryId in it.parentCategoryIds
                val matchesRentalStatus =
                    !onlyRentalAvailable || it.status == ProductStatus.AVAILABLE
                matchesCategory && matchesRentalStatus
            }
}