package com.example.rentit.domain.rental.usecase

import com.example.rentit.common.enums.RentalProcessType
import com.example.rentit.data.rental.dto.TrackingRegistrationRequestDto
import com.example.rentit.data.rental.dto.TrackingRegistrationResponseDto
import com.example.rentit.domain.rental.repository.RentalRepository
import javax.inject.Inject

/**
 * 배송/운송장 정보를 등록하는 UseCase
 *
 * - 예약 ID와 상품 ID를 기반으로 운송장 정보를 서버에 등록
 * - 입력값 검증 후, 유효한 요청만 전송
 */

class RegisterTrackingUseCase @Inject constructor(
    private val rentalRepository: RentalRepository
) {
    suspend operator fun invoke(
        productId: Int,
        reservationId: Int,
        type: RentalProcessType,
        courierName: String,
        trackingNumber: String
    ): Result<TrackingRegistrationResponseDto?> {

        if(trackingNumber.isBlank()) {
            return Result.failure(IllegalArgumentException())
        }

        if(type == RentalProcessType.NONE) {
            return Result.failure(Exception("Tracking Register type is NONE"))
        }

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