package com.example.rentit.domain.product.usecase

import com.example.rentit.data.product.dto.ReservationRequestDto
import com.example.rentit.data.product.dto.ReservationResponseDto
import com.example.rentit.domain.product.repository.ProductRepository
import javax.inject.Inject

/**
 * 상품 예약 요청을 처리하는 UseCase
 *
 * - 선택한 대여 기간이 상품의 최소/최대 대여 기간 범위 내인지 검증
 * - 유효한 경우 서버에 예약 요청 전송
 */

class PostReservationUseCase @Inject constructor(
    private val productRepository: ProductRepository,
) {

    suspend operator fun invoke(
        productId: Int,
        minPeriod: Int?,
        maxPeriod: Int?,
        selectedPeriod: Int,
        startDate: String,
        endDate: String,
    ): Result<ReservationResponseDto> {
        return runCatching {
            val periodRange = (minPeriod ?: 0)..(maxPeriod ?: Int.MAX_VALUE)
            if(selectedPeriod !in periodRange) throw IllegalArgumentException("Invalid period")

            val request = ReservationRequestDto(startDate, endDate)
            productRepository.postReservation(productId, request).getOrThrow()
        }
    }
}