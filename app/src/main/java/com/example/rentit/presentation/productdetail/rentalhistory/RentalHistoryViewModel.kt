package com.example.rentit.presentation.productdetail.rentalhistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.data.product.dto.RequestInfoDto
import com.example.rentit.domain.product.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RentalHistoryViewModel @Inject constructor(
    private val productRepository: ProductRepository,
): ViewModel() {

    private val _requestList =  MutableStateFlow<List<RequestInfoDto>>(emptyList())
    val requestList: StateFlow<List<RequestInfoDto>> = _requestList

    fun getProductRequestList(productId: Int){
        viewModelScope.launch {
            productRepository.getProductRequestList(productId).onSuccess {
                _requestList.value = it.reservations
            }
        }
    }
}