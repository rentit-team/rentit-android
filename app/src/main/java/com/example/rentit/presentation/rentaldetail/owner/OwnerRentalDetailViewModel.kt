package com.example.rentit.presentation.rentaldetail.owner

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentit.data.rental.dto.ChangedByDto
import com.example.rentit.data.rental.dto.DeliveryStatusDto
import com.example.rentit.data.rental.dto.ProductDto
import com.example.rentit.data.rental.dto.RentalDetailResponseDto
import com.example.rentit.data.rental.dto.RentalDto
import com.example.rentit.data.rental.dto.RenterDto
import com.example.rentit.data.rental.dto.ReturnStatusDto
import com.example.rentit.data.rental.dto.StatusHistoryDto
import com.example.rentit.data.rental.repository.RentalRepository
import com.example.rentit.presentation.rentaldetail.owner.stateui.OwnerRentalStatusUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OwnerRentalDetailViewModel @Inject constructor(
    private val rentalRepository: RentalRepository,
) : ViewModel() {
    private val sampleRentalDetail = RentalDetailResponseDto(
        rental = RentalDto(
            reservationId = 1001,
            renter = RenterDto(
                userId = 101,
                nickname = "김철수"
            ),
            status = "RENTING",
            product = ProductDto(
                title = "캐논 EOS R6 카메라",
                thumbnailImgUrl = "https://example.com/image/123.jpg"
            ),
            startDate = "2025-04-15",
            endDate = "2025-04-17",
            totalAmount = 90000,
            depositAmount = 45000,
            rentalTrackingNumber = "1234567890",
            returnTrackingNumber = null,
            deliveryStatus = DeliveryStatusDto(
                isPhotoRegistered = true,
                isTrackingNumberRegistered = true
            ),
            returnStatus = ReturnStatusDto(
                isPhotoRegistered = false,
                isTrackingNumberRegistered = false
            )
        ),
        statusHistory = listOf(
            StatusHistoryDto(
                status = "PENDING",
                changedAt = "2025-03-25T09:00:00Z",
                changedBy = ChangedByDto(
                    userId = 1,
                    nickname = "김숙명"
                )
            )
        )
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private val _uiModel = MutableStateFlow(sampleRentalDetail.toOwnerUiModel())

    @RequiresApi(Build.VERSION_CODES.O)
    val uiModel: StateFlow<OwnerRentalStatusUiModel> = _uiModel

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRentalDetail(productId: Int, reservationId: Int) {
        viewModelScope.launch {
            rentalRepository.getRentalDetail(productId, reservationId)
                .onSuccess {
                    _uiModel.value = it.toOwnerUiModel()
                }.onFailure {

                }
        }
    }
}