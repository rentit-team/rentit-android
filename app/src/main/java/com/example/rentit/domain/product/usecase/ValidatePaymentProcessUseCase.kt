package com.example.rentit.domain.product.usecase

import com.example.rentit.common.enums.RentalStatus
import com.example.rentit.domain.product.model.PaymentValidationResult
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 예약에 대한 사용자의 결제 가능 여부를 확인하는 UseCase
 *
 * 결제 가능 조건:
 * - 현재 사용자가 빌리는 사람일 것
 * - 예약 상태가 [RentalStatus.ACCEPTED]일 것
 * - 상품이 존재할 것
 */

@Singleton
class ValidatePaymentProcessUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    operator fun invoke(productId: Int?, reservationId: Int?, renterId: Long?, rentalStatus: RentalStatus?): PaymentValidationResult {
        val authUserId = userRepository.getAuthUserIdFromPrefs()

        return when {
            authUserId != renterId -> PaymentValidationResult.NotRenter
            rentalStatus != RentalStatus.ACCEPTED -> PaymentValidationResult.InvalidStatus
            productId == null || reservationId == null -> PaymentValidationResult.ProductNotFound
            else -> PaymentValidationResult.Success
        }
    }
}