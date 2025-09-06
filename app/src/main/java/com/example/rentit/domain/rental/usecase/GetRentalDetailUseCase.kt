package com.example.rentit.domain.rental.usecase

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.rentit.common.enums.RentalRole
import com.example.rentit.data.rental.mapper.toModel
import com.example.rentit.domain.rental.exception.RentalStatusUnknownException
import com.example.rentit.domain.rental.model.RentalDetailModel
import com.example.rentit.domain.rental.model.RentalDetailStatusModel
import com.example.rentit.domain.rental.repository.RentalRepository
import com.example.rentit.domain.user.repository.UserRepository
import javax.inject.Inject

class GetRentalDetailUseCase @Inject constructor(
    private val rentalRepository: RentalRepository,
    private val userRepository: UserRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(productId: Int, reservationId: Int): Result<RentalDetailModel> {
        return runCatching {
            val rentalDetail = rentalRepository.getRentalDetail(productId, reservationId).getOrThrow()

            val rentalDetailStatusModel = rentalDetail.toModel()
            if(rentalDetailStatusModel is RentalDetailStatusModel.Unknown) {
                throw RentalStatusUnknownException()
            }

            val authUserId = userRepository.getAuthUserIdFromPrefs()
            val renterId = rentalDetail.rental.renter.userId

            // 현재 로직은 대여 상세에 Renter 또는 Owner만 접근하도록 설계되어 있음
            val role = if(renterId == authUserId) RentalRole.RENTER else RentalRole.OWNER

            RentalDetailModel(
                role = role,
                rentalDetailStatusModel = rentalDetailStatusModel
            )
        }
    }
}