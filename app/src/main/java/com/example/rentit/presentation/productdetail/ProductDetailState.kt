package com.example.rentit.presentation.productdetail

import com.example.rentit.domain.product.model.ProductDetailModel

data class ProductDetailState(
    val productDetail: ProductDetailModel = ProductDetailModel.EMPTY,
    val reservedDateList: List<String> = emptyList(),
    val requestCount: Int? = null,
    val isMyProduct: Boolean = false,
    val isLoading: Boolean = false,
    val showFullImage: Boolean = false,
    val showBottomSheet: Boolean = false
)