package com.example.rentit.feature.product

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.dto.BookingRequestDto
import com.example.rentit.data.product.dto.BookingResponseDto
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
import java.time.LocalDateTime
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

    @RequiresApi(Build.VERSION_CODES.O)
    val sampleReservationsList = listOf(
        RequestInfoDto(
            reservationId = 1001,
            renterNickName = "눈송",
            startDate = "2025-06-15",
            endDate = "2025-06-17",
            status = "PENDING",
            requestedAt = LocalDateTime.of(2025, 3, 20, 14, 0, 0).format(formatter),
            chatRoomId = "room_74248fca-630a-4d25-9e26-a3b943afe300"
        ),
        RequestInfoDto(
            reservationId = 1002,
            renterNickName = "눈송",
            startDate = "2025-07-04",
            endDate = "2025-07-05",
            status = "PENDING",
            requestedAt = LocalDateTime.of(2025, 4, 10, 10, 30, 0).format(formatter),
            chatRoomId = "room_74248fca-630a-4d25-9e26-a3b943afe300"
        ),
        RequestInfoDto(
            reservationId = 21,
            renterNickName = "눈송",
            startDate = "2025-06-27",
            endDate = "2025-07-03",
            status = "PENDING",
            requestedAt = LocalDateTime.of(2025, 6, 25, 16, 15, 0).format(formatter),
            chatRoomId = "room_74248fca-630a-4d25-9e26-a3b943afe300"
        ),
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

    private val _reservedDateList =  MutableStateFlow<List<String>>(emptyList())
    val reservedDateList: StateFlow<List<String>> = _reservedDateList

    private val _bookingResult =  MutableStateFlow<Result<BookingResponseDto>?>(null)
    val bookingResult: StateFlow<Result<BookingResponseDto>?> = _bookingResult

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

    fun getProductRequestList(productId: Int){
        viewModelScope.launch {
            repository.getProductRequestList(productId).onSuccess {
                _requestList.value = it.reservations
            }
        }
    }
}