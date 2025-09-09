package com.example.rentit.domain.rental.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.rental.mapper.toChatRoomSummaryModel
import com.example.rentit.domain.rental.model.RentalChatRoomSummaryModel
import com.example.rentit.domain.rental.repository.RentalRepository
import javax.inject.Inject

/**
 * 특정 예약에 대한 채팅방 요약 정보를 가져오는 UseCase
 *
 * - 상품 ID와 예약 ID를 기반으로 예약 상세 정보를 조회
 * - 예약 정보를 채팅방 요약 도메인 모델(RentalChatRoomSummaryModel)로 변환
 */

class GetChatRoomRentalSummaryUseCase @Inject constructor(
    private val rentalRepository: RentalRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(productId: Int, reservationId: Int): Result<RentalChatRoomSummaryModel> {
        return rentalRepository.getRentalDetail(productId, reservationId)
            .map { it.rental.toChatRoomSummaryModel() }
    }
}