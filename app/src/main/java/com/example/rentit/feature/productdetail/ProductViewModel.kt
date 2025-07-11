package com.example.rentit.feature.productdetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.ResvStatus
import com.example.rentit.data.product.dto.ResvRequestDto
import com.example.rentit.data.product.dto.ResvResponseDto
import com.example.rentit.data.product.dto.ProductDetailResponseDto
import com.example.rentit.data.product.dto.RequestInfoDto
import com.example.rentit.data.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: ProductRepository
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss") // requestedAt 형식을 위한 포맷터

    private val _productId = savedStateHandle.getStateFlow("productId", -1)
    val productId: StateFlow<Int> = _productId

    private val _resvStartDate = MutableStateFlow<LocalDate?>(null)
    val resvStartDate: StateFlow<LocalDate?> = _resvStartDate

    private val _resvEndDate = MutableStateFlow<LocalDate?>(null)
    val resvEndDate: StateFlow<LocalDate?> = _resvEndDate

    private val _formattedTotalPrice = MutableStateFlow<String?>(null)
    val formattedTotalPrice: StateFlow<String?> = _formattedTotalPrice

    @RequiresApi(Build.VERSION_CODES.O)
    val resvPeriod: StateFlow<Int> = combine(
        _resvStartDate,
        _resvEndDate
    ) { start, end ->
        if(start != null && end != null) {
            ChronoUnit.DAYS.between(start, end).toInt() + 1
        } else {
            0
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0 )

    private val _productDetail = MutableStateFlow<Result<ProductDetailResponseDto>?>(null)
    val productDetail: StateFlow<Result<ProductDetailResponseDto>?> = _productDetail

    private val _reservedDateList =  MutableStateFlow<List<String>>(emptyList())
    val reservedDateList: StateFlow<List<String>> = _reservedDateList

    private val _resvResult =  MutableStateFlow<Result<ResvResponseDto>?>(null)
    val resvResult: StateFlow<Result<ResvResponseDto>?> = _resvResult

    private val _requestList =  MutableStateFlow<List<RequestInfoDto>>(emptyList())
    val requestList: StateFlow<List<RequestInfoDto>> = _requestList


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
    fun setResvStartDate(date: LocalDate?) {
        _resvStartDate.value = date
    }
    fun setResvEndDate(date: LocalDate?) {
        _resvEndDate.value = date
    }
    fun setFormattedTotalPrice(formattedPrice: String?) {
        _formattedTotalPrice.value = formattedPrice
    }
    private fun getProductDetail(productId: Int) {
        viewModelScope.launch {
            _productDetail.value = repository.getProductDetail(productId)
        }
    }

    private fun getReservedDates(productId: Int) {
        viewModelScope.launch {
            repository.getReservedDates(productId).onSuccess {
                _reservedDateList.value = it.disabledDates
            }
        }
    }

    fun postResv(productId: Int, startDate: LocalDate, endDate: LocalDate){
        val request = ResvRequestDto(startDate.toString(), endDate.toString())
        viewModelScope.launch {
            _resvResult.value = repository.postResv(productId, request)
        }
    }

    fun getProductRequestList(productId: Int){
        viewModelScope.launch {
            repository.getProductRequestList(productId).onSuccess {
                _requestList.value = it.reservations.filter { data -> ResvStatus.isPending(data.status) }
            }
        }
    }
}