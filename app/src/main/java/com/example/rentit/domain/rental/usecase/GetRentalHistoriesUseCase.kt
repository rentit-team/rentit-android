package com.example.rentit.domain.rental.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.rental.mapper.toModel
import com.example.rentit.domain.rental.model.RentalHistoryModel
import com.example.rentit.domain.rental.repository.RentalRepository
import javax.inject.Inject


/**
 * 특정 상품(productId)에 대한 대여 내역을 조회하는 UseCase
 *
 * - RentalRepository를 통해 서버에서 상품별 대여 내역 조회
 * - 가져온 DTO를 도메인 모델(RentalHistoryModel)로 변환
 */

class GetRentalHistoriesUseCase @Inject constructor(
    private val rentalRepository: RentalRepository
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(productId: Int): Result<List<RentalHistoryModel>> {
        return runCatching {
            val rentalHistories = rentalRepository.getRentalHistoriesByProduct(productId).getOrThrow()
            rentalHistories.reservations.map { it.toModel() }
        }
    }
}