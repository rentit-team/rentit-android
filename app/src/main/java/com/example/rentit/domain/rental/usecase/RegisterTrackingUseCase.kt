package com.example.rentit.domain.rental.usecase

import com.example.rentit.common.enums.TrackingRegistrationRequestType
import com.example.rentit.data.rental.dto.TrackingRegistrationRequestDto
import com.example.rentit.data.rental.dto.TrackingRegistrationResponseDto
import com.example.rentit.domain.rental.repository.RentalRepository
import javax.inject.Inject

class RegisterTrackingUseCase @Inject constructor(
    private val rentalRepository: RentalRepository
) {
    suspend operator fun invoke(
        productId: Int,
        reservationId: Int,
        type: TrackingRegistrationRequestType,
        courierName: String,
        trackingNumber: String
    ): Result<TrackingRegistrationResponseDto?> {
        if(trackingNumber.isBlank())
            return Result.failure(IllegalArgumentException())
        if(type == TrackingRegistrationRequestType.NONE)
            return Result.failure(Exception())

        return rentalRepository.postTrackingRegistration(
            productId = productId,
            reservationId = reservationId,
            body = TrackingRegistrationRequestDto(
                type = type.name,
                courierName = courierName,
                trackingNumber = trackingNumber
            )
        )
    }
}