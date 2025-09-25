package com.example.rentit.presentation.main.createpost

import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import com.example.rentit.common.util.PRICE_LIMIT
import com.example.rentit.domain.product.model.CategoryModel

data class CreatePostState(
    val title: String = "",
    val content: String = "",
    val categoryMap: Map<Int, CategoryModel> = emptyMap(),
    val selectedCategoryIdList: List<Int> = emptyList(),
    val selectedImgUriList: List<Uri> = emptyList(),
    val periodSliderPosition: ClosedFloatingPointRange<Float> = 3F..15F,
    val priceTextFieldValue: TextFieldValue = TextFieldValue(""),
    val isLoading: Boolean = false,
    val showEmptyTitleError: Boolean = false,
    val showEmptyContentError: Boolean = false,
    val showEmptyPriceError: Boolean = false,
    val showCategoryTagDrawer: Boolean = false,
) {
    val price: Int
        get() {
            val digitsOnly = priceTextFieldValue.text.filter { v -> v.isDigit() }
            return digitsOnly.toIntOrNull()?.coerceAtMost(PRICE_LIMIT) ?: 0
        }

    val isPostButtonAvailable: Boolean
        get() = !isLoading
}