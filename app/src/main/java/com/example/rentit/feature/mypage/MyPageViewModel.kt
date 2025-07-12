package com.example.rentit.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.product.dto.ProductDto
import com.example.rentit.data.user.dto.ReservationDto
import com.example.rentit.data.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _myProductList = MutableStateFlow<List<ProductDto>>(emptyList())
    val myProductList: StateFlow<List<ProductDto>> = _myProductList

    private val _myRentalList = MutableStateFlow<List<ReservationDto>>(emptyList())
    val myRentalList: StateFlow<List<ReservationDto>> = _myRentalList

    fun getMyProductList() {
        viewModelScope.launch {
            userRepository.getMyProductList().onSuccess {
                _myProductList.value = it.myProducts
            }.onFailure {
                /* 로딩 실패 시 */
            }
        }
    }

    fun getMyRentalList() {
        viewModelScope.launch {
            userRepository.getMyRentalList().onSuccess {
                _myRentalList.value = it.myReservations
            }.onFailure {
                /* 로딩 실패 시 */
            }
        }
    }
}