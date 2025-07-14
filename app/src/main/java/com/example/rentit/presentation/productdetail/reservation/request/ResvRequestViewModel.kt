package com.example.rentit.presentation.productdetail.reservation.request

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.dto.ResvRequestDto
import com.example.rentit.data.product.dto.ResvResponseDto
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
class ResvRequestViewModel @Inject constructor(
    private val productRepository: ProductRepository,
): ViewModel() {

    private val _reservedDateList =  MutableStateFlow<List<String>>(emptyList())
    val reservedDateList: StateFlow<List<String>> = _reservedDateList

    private val _rentalStartDate = MutableStateFlow<LocalDate?>(null)
    val rentalStartDate: StateFlow<LocalDate?> = _rentalStartDate

    private val _rentalEndDate = MutableStateFlow<LocalDate?>(null)
    val rentalEndDate: StateFlow<LocalDate?> = _rentalEndDate

    private val _formattedTotalPrice = MutableStateFlow<String?>(null)
    val formattedTotalPrice: StateFlow<String?> = _formattedTotalPrice

    private val _productPrice = MutableStateFlow(0)
    val productPrice: StateFlow<Int> = _productPrice

    private val _resvResult =  MutableStateFlow<Result<ResvResponseDto>?>(null)
    val resvResult: StateFlow<Result<ResvResponseDto>?> = _resvResult

    @RequiresApi(Build.VERSION_CODES.O)
    val rentalPeriod: StateFlow<Int> = combine(
        _rentalStartDate,
        _rentalEndDate
    ) { start, end ->
        if(start != null && end != null) {
            ChronoUnit.DAYS.between(start, end).toInt() + 1
        } else {
            0
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0 )

    fun setRentalStartDate(date: LocalDate?) {
        _rentalStartDate.value = date
    }

    fun setRentalEndDate(date: LocalDate?) {
        _rentalEndDate.value = date
    }

    fun setFormattedTotalPrice(formattedPrice: String?) {
        _formattedTotalPrice.value = formattedPrice
    }

    fun getProductDetail(productId: Int) {
        viewModelScope.launch {
            productRepository.getProductDetail(productId)
                .onSuccess {
                    _productPrice.value = it.product.price
                }.onFailure {  }
        }
    }

    fun getReservedDates(productId: Int) {
        viewModelScope.launch {
            productRepository.getReservedDates(productId).onSuccess {
                _reservedDateList.value = it.disabledDates
            }
        }
    }

    fun postResv(productId: Int){
        val request = ResvRequestDto(_rentalStartDate.value.toString(), _rentalEndDate.value.toString())
        viewModelScope.launch {
            _resvResult.value = productRepository.postResv(productId, request)
        }
    }
}