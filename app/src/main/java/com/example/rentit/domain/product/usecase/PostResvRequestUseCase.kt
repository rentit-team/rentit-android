package com.example.rentit.domain.product.usecase

import com.example.rentit.data.product.dto.ResvRequestDto
import com.example.rentit.domain.product.repository.ProductRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 *  선택한 기간이 유효한지 확인하고 예약 요청을 보내는 UseCase
 */

@Singleton
class PostResvRequestUseCase @Inject constructor(
    private val productRepository: ProductRepository,
) {

    suspend operator fun invoke(
        productId: Int,
        minPeriod: Int?,
        maxPeriod: Int?,
        selectedPeriod: Int,
        startDate: String,
        endDate: String,
    ): Result<Unit> {
        return runCatching {
            val periodRange = (minPeriod ?: 0)..(maxPeriod ?: Int.MAX_VALUE)
            if(selectedPeriod !in periodRange) throw IllegalArgumentException("Invalid period")

            val request = ResvRequestDto(startDate, endDate)
            productRepository.postResv(productId, request).getOrThrow()
        }
    }
}