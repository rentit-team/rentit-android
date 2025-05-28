package com.example.rentit.feature.product

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.dto.BookingRequestDto
import com.example.rentit.data.product.dto.BookingResponseDto
import com.example.rentit.data.product.dto.Category
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
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

    private val _productId = savedStateHandle.getStateFlow("productId", -1)
    val productId: StateFlow<Int> = _productId

    private val _bookingStartDate = MutableStateFlow<LocalDate?>(null)
    val bookingStartDate: StateFlow<LocalDate?> = _bookingStartDate

    private val _bookingEndDate = MutableStateFlow<LocalDate?>(null)
    val bookingEndDate: StateFlow<LocalDate?> = _bookingEndDate

    private val _formattedTotalPrice = MutableStateFlow<String?>(null)
    val formattedTotalPrice: StateFlow<String?> = _formattedTotalPrice

    @RequiresApi(Build.VERSION_CODES.O)
    val bookingPeriod: StateFlow<Int> = combine(
        _bookingStartDate,
        _bookingEndDate
    ) { start, end ->
        if(start != null && end != null) {
            ChronoUnit.DAYS.between(start, end).toInt() + 1
        } else {
            0
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0 )

    private val _productDetail = MutableStateFlow<Result<ProductDetailResponseDto>?>(null)
    val productDetail: StateFlow<Result<ProductDetailResponseDto>?> = _productDetail

    private val _categoryTagList =  MutableStateFlow<List<Category>>(emptyList())
    val categoryTagList: StateFlow<List<Category>> = _categoryTagList

    private val _selectedImgUriList =  MutableStateFlow<List<Uri>>(emptyList())
    val selectedImgUriList: StateFlow<List<Uri>> = _selectedImgUriList

    private val _reservedDateList =  MutableStateFlow<List<String>>(emptyList())
    val reservedDateList: StateFlow<List<String>> = _reservedDateList

    private val _bookingResult =  MutableStateFlow<Result<BookingResponseDto>?>(null)
    val bookingResult: StateFlow<Result<BookingResponseDto>?> = _bookingResult

    init {
        viewModelScope.launch {
            _productId.collect { id ->
                if (id > -1) {
                    getProductDetail(id)
                    getReservedDates(id)
                }
            }
        }
    }

    fun setProductId(id: Int) {
        viewModelScope.launch {
            if(id != _productId.value){
                savedStateHandle["productId"] = id
            }
        }
    }
    fun setBookingStartDate(date: LocalDate?) {
        _bookingStartDate.value = date
    }
    fun setBookingEndDate(date: LocalDate?) {
        _bookingEndDate.value = date
    }
    fun setFormattedTotalPrice(formattedPrice: String?) {
        _formattedTotalPrice.value = formattedPrice
    }
    private fun getProductDetail(productId: Int) {
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

    fun updateImageUriList(uriList: List<Uri>) {
        _selectedImgUriList.value = uriList
    }

    fun removeImageUri(uri: Uri){
        _selectedImgUriList.value = _selectedImgUriList.value - uri
    }

    private fun getReservedDates(productId: Int) {
        viewModelScope.launch {
            repository.getReservedDates(productId).onSuccess {
                _reservedDateList.value = it.disabledDates
            }
        }
    }

    fun postBooking(productId: Int, startDate: LocalDate, endDate: LocalDate){
        val request = BookingRequestDto(startDate.toString(), endDate.toString())
        viewModelScope.launch {
            _bookingResult.value = repository.postBooking(productId, request)
        }
    }
}