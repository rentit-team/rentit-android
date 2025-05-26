package com.example.rentit.feature.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.dto.Category
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    val sampleCategoryList = listOf(
        Category(1, "취미", true, null),
        Category(2, "태그2", false, 1),
        Category(3, "태그3", false, 1),
        Category(4, "태그4", false, 1),
        Category(5, "스포츠", true, null),
        Category(6, "태그22", false, 5),
        Category(7, "태그22", false, 5),
        Category(8, "태그2sdfasdfasdf2", false, 5),
        Category(9, "태그2sdfasdfasdf2", false, 5),
        Category(10, "태그22", false, 5),
    )

    private val _productDetail = MutableStateFlow<Result<ProductDetailResponseDto>?>(null)
    val productDetail: StateFlow<Result<ProductDetailResponseDto>?> = _productDetail

    private val _categoryTagList =  MutableStateFlow<List<Category>>(emptyList())
    val categoryTagList: StateFlow<List<Category>> = _categoryTagList

    fun getProductDetail(productId: Int) {
        viewModelScope.launch {
            _productDetail.value = repository.getProductDetail(productId)
        }
    }

    fun handleCategoryClick(category: Category) {
        if(_categoryTagList.value.contains(category)){
            _categoryTagList.value = _categoryTagList.value - category
        } else {
            _categoryTagList.value = _categoryTagList.value + category
        }
    }

    fun removeSelectedCategory(category: Category) {
        if(_categoryTagList.value.contains(category)){
            _categoryTagList.value = _categoryTagList.value - category
        }
    }
}