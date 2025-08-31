package com.example.rentit.domain.rental.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.data.rental.mapper.toChatRoomSummaryModel
import com.example.rentit.domain.rental.model.RentalChatRoomSummaryModel
import com.example.rentit.domain.rental.repository.RentalRepository
import javax.inject.Inject

class GetChatRoomRentalSummaryUseCase @Inject constructor(
    private val rentalRepository: RentalRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(productId: Int, reservationId: Int): Result<RentalChatRoomSummaryModel> {
        return rentalRepository.getRentalDetail(productId, reservationId)
            .map { it.rental.toChatRoomSummaryModel() }
    }
}