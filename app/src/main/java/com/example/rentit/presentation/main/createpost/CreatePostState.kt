package com.example.rentit.presentation.main.createpost

import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import com.example.rentit.common.PRICE_LIMIT
import com.example.rentit.data.product.dto.CategoryDto

data class CreatePostState(
    val title: String = "",
    val content: String = "",
    val categoryList: List<CategoryDto> = emptyList(),
    val selectedCategoryList: List<CategoryDto> = emptyList(),
    val selectedImgUriList: List<Uri> = emptyList(),
    val periodSliderPosition: ClosedFloatingPointRange<Float> = 3F..15F,
    val priceTextFieldValue: TextFieldValue = TextFieldValue(""),
    val showCategoryTagDrawer: Boolean = false,
) {
    val price: Int
        get() {
            val digitsOnly = priceTextFieldValue.text.filter { v -> v.isDigit() }
            return digitsOnly.toIntOrNull()?.coerceAtMost(PRICE_LIMIT) ?: 0
        }
}